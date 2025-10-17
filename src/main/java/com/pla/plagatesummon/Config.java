package com.pla.plagatesummon;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> GATES;
    public static ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static ForgeConfigSpec.IntValue SPAWN_RATE;
    public static ForgeConfigSpec.IntValue SPAWN_DAY;

    static {
        DEBUG_MODE = BUILDER.comment("Turn on debug mode").define("debug_mode", true);
        SPAWN_RATE = BUILDER.comment("Spawn rate percentage (1–100). Used if spawn_day is 0")
                .defineInRange("spawn_rate", 20, 1, 100);
        SPAWN_DAY = BUILDER.comment("Force gate spawn after this in-game day. If set above 0, overrides spawn_rate and guarantees 100% spawn")
                .defineInRange("spawn_day", 0, 0, Integer.MAX_VALUE);
        GATES = BUILDER.comment("A list of gates to be spawned")
                .defineList("whiteList", List.of(
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
}