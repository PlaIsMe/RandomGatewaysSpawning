package com.pla.plagatesummon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = PlaGateSummon.MOD_ID)
public class SleepPreventionHandler {
    @SubscribeEvent
    public static void onCanPlayerSleep(CanPlayerSleepEvent event) {
        Player entity = event.getEntity();

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null || !world.dimension().equals(Level.OVERWORLD)) return;

        GateSpawnData data = GateSpawnData.get(world);

        if (data.spawnPos != null) {
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
            entity.displayClientMessage(
                    Component.literal("A strange force prevents you from sleeping ...").withStyle(ChatFormatting.RED),
                    false
            );
        }
    }
}
