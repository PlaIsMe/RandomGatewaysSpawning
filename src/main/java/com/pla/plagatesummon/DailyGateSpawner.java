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
    private static int nextSpawnTick = -1;
    private static boolean shouldSpawnToday = false;
    private static BlockPos spawnPos = null;
    private static boolean isPromptPlayer = false;
    private static String mainMessage = "";
    private static int hexColor = 0;
    private static String randomGate;
    private static String subMessage = "";
    private static String waypointName = "";

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.START) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null || !world.dimension().equals(Level.OVERWORLD)) return;

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
        long dayTime = world.getDayTime() % 24000; // Current time in the day

        // Check if it's a new day (midnight tick)
        if (dayTime == 1) {
            if (spawnPos != null) {
                shouldSpawnToday = false;
                nextSpawnTick = -1;
                spawnPos = null;
                if (isPromptPlayer) {
                    NotificationOverlay.showNotification("You slept soundly, unaware... The dark forces have faded.", 0xAA00FF);
                    isPromptPlayer = false;
                }
            }
            shouldSpawnToday = random.nextBoolean(); // 50% chance
            if (shouldSpawnToday) {
                nextSpawnTick = (120 + random.nextInt(2280)) * 10;
                spawnPos = null;
//                LOGGER.info("Random gate will be spawned to day at " + nextSpawnTick);
            } else {
                return;
            }
        }

        if (!shouldSpawnToday) return;

        long remainingTick = nextSpawnTick - dayTime;

        if (remainingTick <= 0 && spawnPos != null) {
            String summonCommand = "open_gateway " + spawnPos.getX() + " " + spawnPos.getY() + " " + spawnPos.getZ() + " " + randomGate;
            try {
                Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(summonCommand, source);
            } catch (CommandSyntaxException e) {
                LOGGER.error("Failed to execute command {}, error {}", summonCommand, e);
            }
            shouldSpawnToday = false;
            nextSpawnTick = -1;
            spawnPos = null;
            isPromptPlayer = false;

        } else if (remainingTick <= 6000) {
            if (!isPromptPlayer) {
                String clearWaypoint = "waypoint delete \"" + waypointName + "\" @a";
                try {
                    Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(clearWaypoint, source);
                } catch (CommandSyntaxException e) {
                    LOGGER.error("Failed to execute command {}, error {}", clearWaypoint, e);
                }

                if (spawnPos == null) {
                    List<ServerPlayer> players = world.players();
                    if (!players.isEmpty()) {
                        ServerPlayer targetPlayer = players.get(random.nextInt(players.size()));
                        spawnPos = SurfaceSpawnHelper.findRandomSurfacePos(world, targetPlayer.blockPosition(), 50, 300);
                    } else {
                        shouldSpawnToday = false;
                        nextSpawnTick = -1;
                        spawnPos = null;
                        isPromptPlayer = false;
                        return;
                    }
                }

                List<? extends List<? extends String>> gates = Config.GATES.get();
                List<? extends String> randomGateData = gates.get(world.random.nextInt(gates.size()));
                randomGate = randomGateData.get(0);
                hexColor = (0xFF << 24) | Integer.parseInt(randomGateData.get(1).substring(1), 16);
                mainMessage = randomGateData.get(2);
                subMessage = randomGateData.get(3);
                waypointName = randomGateData.get(4);

                String addWaypoint = "waypoint create \"" + waypointName + "\" minecraft:overworld " + spawnPos.getX() + " " + spawnPos.getY() + " " + spawnPos.getZ() + " dark_purple @a";
                try {
                    Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(addWaypoint, source);
                } catch (CommandSyntaxException e) {
                    LOGGER.error("Failed to execute command {}, error {}", addWaypoint, e);
                }
                isPromptPlayer = true;
            }
            assert spawnPos != null;
            if (spawnPos.getY() == 0) {
                if (SurfaceSpawnHelper.isChunkLoaded(world, spawnPos)) {
                    spawnPos = SurfaceSpawnHelper.moveToSurface(world, spawnPos);
                }
            }
            NotificationOverlay.showNotification(mainMessage + " at x: " + spawnPos.getX() + " z: " + spawnPos.getZ() + " in " + (remainingTick / 20) + " seconds! " + subMessage, hexColor);
        }
    }
}
