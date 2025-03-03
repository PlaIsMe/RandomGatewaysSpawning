package com.pla.plagatesummon;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class NotificationOverlay {
    private static final int MESSAGE_LIFETIME = 20;
    private static String message = "";
    private static int messageTimer = 0;
    private static int hexColor = 0;

    public static void showNotification(String newMessage, int newHexColor) {
        message = newMessage;
        hexColor = newHexColor;
        messageTimer = MESSAGE_LIFETIME;
    }

    // Reduce the timer on every client tick (20 ticks per second)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && messageTimer > 0) {
            messageTimer--;
        }
    }

    @SubscribeEvent
    public static void onRenderNotificationOverlay(RenderGameOverlayEvent.Text event) {
        if (messageTimer <= 0) return; // No active message

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        PoseStack poseStack = event.getMatrixStack();
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int x = (screenWidth - font.width(message)) / 2;
        int y = screenHeight - 60;

        font.draw(poseStack, message, x, y, hexColor);
    }
}
