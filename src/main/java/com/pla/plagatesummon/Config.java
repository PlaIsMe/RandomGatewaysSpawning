package com.pla.plagatesummon;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

@EventBusSubscriber(modid = PlaGateSummon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static ModConfigSpec.ConfigValue<List<? extends String>> GATES;
    public static ModConfigSpec.ConfigValue<Integer> SPAWN_RATE;
    public static ModConfigSpec.ConfigValue<Integer> SPAWN_DAY;

    static {
        DEBUG_MODE = BUILDER.comment("Turn on debug mode")
                .define("debug_mode", true);
        SPAWN_RATE = BUILDER.comment("Spawn rate percentage (1–100). Used if spawn_day is 0")
                .defineInRange("spawn_rate", 20, 1, 100);
        SPAWN_DAY = BUILDER.comment("Force gate spawn after this in-game day. If set above 0, overrides spawn_rate and guarantees 100% spawn")
                .defineInRange("spawn_day", 0, 0, Integer.MAX_VALUE);
        GATES = BUILDER.comment("A list of gates to be spawned")
                .defineList("gates", List.of(
                        "gateways:basic/blaze",
                        "gateways:basic/enderman",
                        "gateways:basic/slime",
                        "gateways:emerald_grove",
                        "gateways:endless/blaze",
                        "gateways:hellish_fortress",
                        "gateways:overworldian_nights"
                ), entry -> entry instanceof String);
        SPEC = BUILDER.build();
    }

    private static boolean validateGateFormat(Object obj) {
        if (!(obj instanceof List<?> list)) return false;
        return !list.isEmpty() && list.stream().allMatch(e -> e instanceof String);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
    }
}
