package com.pla.plagatesummon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = PlaGateSummon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SleepPreventionHandler {
    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null || !world.dimension().equals(Level.OVERWORLD)) return;

        GateSpawnData data = GateSpawnData.get(world);

        if (data.spawnPos != null) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.displayClientMessage(
                    Component.literal("A strange force prevents you from sleeping ...").withStyle(ChatFormatting.RED),
                    false
            );
        }
    }
}
