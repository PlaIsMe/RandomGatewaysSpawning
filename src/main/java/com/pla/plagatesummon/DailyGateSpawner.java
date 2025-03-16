package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(modid = PlaGateSummon.MOD_ID)
public class DailyGateSpawner {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random random = new Random();
    private static final int radius = 5;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.START) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null || !world.dimension().equals(Level.OVERWORLD)) return;

        GateSpawnData data = GateSpawnData.get(world);

        ServerPlayer pPlayer = world.players().isEmpty() ? null : world.players().get(0);
        if (pPlayer == null) return;

        CommandSourceStack source = pPlayer.createCommandSourceStack();
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
            if (data.spawnPos != null) {
                data.shouldSpawnToday = false;
                data.nextSpawnTick = -1;
                data.spawnPos = null;
                if (data.isPromptPlayer) {
                    NotificationOverlay.showNotification("You slept soundly, unaware... The dark forces have faded.", 0xAA00FF);
                    data.isPromptPlayer = false;
                }
                data.setDirty();
            } else {
                int chunkX = data.oldSpawnPos.getX() >> 4;
                int chunkZ = data.oldSpawnPos.getZ() >> 4;

                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        world.setChunkForced(chunkX + x, chunkZ + z, false);
                    }
                }
                LOGGER.info("PlaGateSummon: Unforced load chunks from (" + (chunkX - radius) + ", " + (chunkZ - radius) + ") to (" + (chunkX + radius) + ", " + (chunkZ + radius) + ")");
            }
            data.shouldSpawnToday = random.nextBoolean(); // 50% chance
            if (data.shouldSpawnToday) {
                data.nextSpawnTick = (120 + random.nextInt(2280)) * 10;
                data.spawnPos = null;
                LOGGER.info("PlaGateSummon: Random gate will be spawned to day at " + data.nextSpawnTick);
            } else {
                return;
            }
            data.setDirty();
        }

        if (!data.shouldSpawnToday) return;

        long remainingTick = data.nextSpawnTick - dayTime;

        if (remainingTick <= 0 && data.spawnPos != null) {
            if (data.spawnPos.getY() == 0) {
                if (SurfaceSpawnHelper.isChunkLoaded(world, data.spawnPos)) {
                    data.spawnPos = SurfaceSpawnHelper.moveToSurface(world, data.spawnPos);
                    data.setDirty();
                    return;
                }
            }
            String summonCommand = "open_gateway " + data.spawnPos.getX() + " " + data.spawnPos.getY() + " " + data.spawnPos.getZ() + " " + data.randomGate;
            int chunkX = data.spawnPos.getX() >> 4;
            int chunkZ = data.spawnPos.getZ() >> 4;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    world.setChunkForced(chunkX + x, chunkZ + z, true);
                }
            }
            LOGGER.info("PlaGateSummon: Forced load chunks from (" + (chunkX - radius) + ", " + (chunkZ - radius) + ") to (" + (chunkX + radius) + ", " + (chunkZ + radius) + ")");
            try {
                Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(summonCommand, source);
            } catch (CommandSyntaxException e) {
                LOGGER.error("Failed to execute command {}, error {}", summonCommand, e);
            }
            data.shouldSpawnToday = false;
            data.nextSpawnTick = -1;
            data.oldSpawnPos = data.spawnPos;
            data.spawnPos = null;
            data.isPromptPlayer = false;
            data.setDirty();
        } else if (remainingTick <= 6000) {
            if (!data.isPromptPlayer) {
                String clearWaypoint = "waypoint delete \"" + data.waypointName + "\" @a";
                try {
                    Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(clearWaypoint, source);
                } catch (CommandSyntaxException e) {
                    LOGGER.error("Failed to execute command {}, error {}", clearWaypoint, e);
                }

                if (data.spawnPos == null) {
                    List<ServerPlayer> players = world.players();
                    if (!players.isEmpty()) {
                        ServerPlayer targetPlayer = players.get(random.nextInt(players.size()));
                        data.spawnPos = SurfaceSpawnHelper.findRandomSurfacePos(world, targetPlayer.blockPosition(), 50, 300);
                        data.setDirty();
                    } else {
                        data.shouldSpawnToday = false;
                        data.nextSpawnTick = -1;
                        data.spawnPos = null;
                        data.isPromptPlayer = false;
                        data.setDirty();
                        return;
                    }
                }

                List<? extends List<? extends String>> gates = Config.GATES.get();
                List<? extends String> randomGateData = gates.get(world.random.nextInt(gates.size()));
                data.randomGate = randomGateData.get(0);
                data.hexColor = (0xFF << 24) | Integer.parseInt(randomGateData.get(1).substring(1), 16);
                data.mainMessage = randomGateData.get(2);
                data.subMessage = randomGateData.get(3);
                data.waypointName = randomGateData.get(4);
                data.setDirty();

                String addWaypoint = "waypoint create \"" + data.waypointName + "\" minecraft:overworld " + data.spawnPos.getX() + " " + data.spawnPos.getY() + " " + data.spawnPos.getZ() + " dark_purple @a";
                try {
                    Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(addWaypoint, source);
                } catch (CommandSyntaxException e) {
                    LOGGER.error("Failed to execute command {}, error {}", addWaypoint, e);
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
