package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import shadows.gateways.entity.GatewayEntity;
import shadows.gateways.gate.Gateway;
import shadows.gateways.gate.GatewayManager;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.pla.plagatesummon.WaypointHelper.createWaypoint;

@Mod.EventBusSubscriber(modid = PlaGateSummon.MOD_ID)
public class DailyGateSpawner {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random random = new Random();
    public static ServerPlayer randomPlayer = null;
    public static CommandSourceStack source = null;

    static void resetValue(GateSpawnData data, boolean skippedDay) {
        data.shouldSpawnToday = false;
        data.nextSpawnTick = -1;
        data.spawnPos = null;
        data.isPromptPlayer = false;
        data.skippedToday = skippedDay;
        data.spawnChance = Config.SPAWN_RATE.get();
        data.dayPassed = 1;
        data.setDirty();
        randomPlayer = null;
        source = null;
    }

    static void prepareForSpawning(GateSpawnData data, ServerLevel world, MinecraftServer server, CommandSourceStack source, boolean debug_mode) {
        data.shouldSpawnToday = true;
        data.nextSpawnTick = (120 + random.nextInt(2280)) * 10;
        data.spawnPos = SurfaceSpawnHelper.findRandomSurfacePos(world, randomPlayer.blockPosition(), 50, 300);
        server.getPlayerList().broadcastSystemMessage(
                Component.literal("The gate will open today… but to where?")
                        .withStyle(ChatFormatting.LIGHT_PURPLE),
                false
        );
        if (debug_mode) LOGGER.info("PlaGateSummon: Random gate will be spawned today at " + data.nextSpawnTick + " x: " + data.spawnPos.getX() + " y: " + data.spawnPos.getY() + " z: " + data.spawnPos.getZ());
        data.setDirty();
    }

    static void spawnGateway(ServerLevel world, BlockPos pos, String gatewayId, GateSpawnData data) {
        try {
            String[] parts = gatewayId.split(":");
            ResourceLocation type = ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]);
            Gateway gateway = GatewayManager.INSTANCE.getValue(type);
            if (gateway == null) {
                LOGGER.error("PlaGateSummon: Unknown Gateway: {}", gatewayId);
            }
            GatewayEntity entity = new GatewayEntity(world, randomPlayer, gateway);
            entity.moveTo(Vec3.atCenterOf(pos));
            world.addFreshEntity(entity);
            entity.getPersistentData().putIntArray("GateSpawnPos", new int[]{data.spawnPos.getX(), data.spawnPos.getY(), data.spawnPos.getZ()});
            entity.getPersistentData().putString("GateWaypointName", data.waypointName);
            entity.getPersistentData().putString("UnClaimUUID", randomPlayer.getStringUUID());
            entity.onGateCreated();
        } catch (Exception e) {
            LOGGER.error("PlaGateSummon: Failed to spawn gateway: {}", gatewayId);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.START) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null || !world.dimension().equals(Level.OVERWORLD)) return;

        GateSpawnData data = GateSpawnData.get(world);
        randomPlayer = world.getRandomPlayer();

        boolean debug_mode = Config.DEBUG_MODE.get();
        if (randomPlayer == null) return;

        source = randomPlayer.createCommandSourceStack();
        source = new CommandSourceStack(
                Objects.requireNonNull(source.getEntity()),
                source.getPosition(),
                source.getRotation(),
                source.getLevel(),
                4,
                source.getTextName(),
                source.getDisplayName(),
                source.getServer(),
                source.getEntity()
        );

        long dayTime = world.getDayTime() % 24000;

        if (dayTime == 1) {
            if (data.skippedToday) {
                data.skippedToday = false;
                data.setDirty();
            } else {
                if (data.spawnPos == null) {
                    int spawn_day = Config.SPAWN_DAY.get();
                    if (spawn_day != 0) {
                        if (debug_mode) LOGGER.info("PlaGateSummon: day passed: " + data.dayPassed + " gate will be spawned in " + (spawn_day - data.dayPassed) + " day(s)");
                        if (data.dayPassed == spawn_day) {
                            prepareForSpawning(data, world, server, source, debug_mode);
                        } else {
                            data.shouldSpawnToday = false;
                            data.dayPassed += 1;
                            data.setDirty();
                            if (debug_mode) LOGGER.info("PlaGateSummon: No gate will be spawned today");
                            return;
                        }
                    } else {
                        int randomPercentage = random.nextInt(100);
                        if (debug_mode) LOGGER.info("PlaGateSummon: randomPercentage is " + randomPercentage + " the spawnChance is " + data.spawnChance);
                        if (randomPercentage < data.spawnChance) {
                            prepareForSpawning(data, world, server, source, debug_mode);
                        } else {
                            data.shouldSpawnToday = false;
                            data.spawnChance += Config.SPAWN_RATE.get();
                            data.setDirty();
                            if (debug_mode) LOGGER.info("PlaGateSummon: No gate will be spawned today");
                            return;
                        }
                    }
                }
            }
        }

        if (!data.shouldSpawnToday) return;

        long remainingTick = data.nextSpawnTick - dayTime;

        // Cancel the gate attempt to spawn but still not skip the next day
        if (remainingTick < -6000 && data.spawnPos != null && data.spawnPos.getY() == 0) {
            resetValue(data, false);
        } else if (remainingTick <= 0 && data.spawnPos != null) {
            if (data.spawnPos.getY() == 0) {
                if (SurfaceSpawnHelper.isChunkLoaded(world, data.spawnPos)) {
                    data.spawnPos = SurfaceSpawnHelper.moveToSurface(world, data.spawnPos);
                    data.setDirty();
                    return;
                }
            }

            spawnGateway(world, data.spawnPos, data.randomGate, data);
            resetValue(data, true);
        } else if (remainingTick <= 6000) {
            if (!data.isPromptPlayer) {
                List<? extends List<? extends String>> gates = Config.GATES.get();
                List<? extends String> randomGateData = gates.get(world.random.nextInt(gates.size()));
                data.randomGate = randomGateData.get(0);
                data.hexColor = (0xFF << 24) | Integer.parseInt(randomGateData.get(1).substring(1), 16);
                if (randomGateData.get(2).length() == 0) {
                    data.mainMessage = "A " + randomGateData.get(4) + " is going to spawn";
                } else {
                    data.mainMessage = randomGateData.get(2);
                }
                if (randomGateData.get(3).length() == 0) {
                    data.subMessage = "Be ready to fight!";
                } else {
                    data.subMessage = randomGateData.get(3);
                }

                data.waypointName = randomGateData.get(4);
                data.setDirty();

                createWaypoint(data.spawnPos, data.waypointName, data.hexColor);
                String addWaypoint = "waypoint create \"" + data.waypointName + "\" minecraft:overworld " + data.spawnPos.getX() + " " + data.spawnPos.getY() + " " + data.spawnPos.getZ() + " dark_purple @a";
                try {
                    Objects.requireNonNull(randomPlayer.getServer()).getCommands().getDispatcher().execute(addWaypoint, source);
                } catch (CommandSyntaxException e) {
                    LOGGER.warn("PlaGateSummon: (Journey Map Compat) Failed to execute command {}, error {}", addWaypoint, e);
                }

                data.isPromptPlayer = true;
                data.setDirty();
            }
            assert data.spawnPos != null;
            if (data.spawnPos.getY() == 0) {
                if (SurfaceSpawnHelper.isChunkLoaded(world, data.spawnPos)) {
                    data.spawnPos = SurfaceSpawnHelper.moveToSurface(world, data.spawnPos);
                    data.setDirty();
                }
            }
            NotificationOverlay.showNotification(data.mainMessage + " at x: " + data.spawnPos.getX() + " z: " + data.spawnPos.getZ() + " in " + (remainingTick / 20) + " seconds! " + data.subMessage, data.hexColor);
        }
    }
}
