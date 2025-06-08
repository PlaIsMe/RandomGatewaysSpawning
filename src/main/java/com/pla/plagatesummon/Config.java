package com.pla.plagatesummon;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> GATES;
    public static ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static ForgeConfigSpec.IntValue SPAWN_RATE;
    public static ForgeConfigSpec.IntValue SPAWN_DAY;

    static {
        DEBUG_MODE = BUILDER.comment("Turn on debug mode").define("debug_mode", true);
        SPAWN_RATE = BUILDER.comment("Spawn rate percentage (1–100). Used if spawn_day is 0")
                .defineInRange("spawn_rate", 20, 1, 100);
        SPAWN_DAY = BUILDER.comment("Force gate spawn after this in-game day. If set above 0, overrides spawn_rate and guarantees 100% spawn")
                .defineInRange("spawn_day", 0, 0, Integer.MAX_VALUE);
        GATES = BUILDER.comment(
                "A list of gates to be spawned with attributes",
                "Format: [<gate_id>, <gate_color>, <main_message>, <sub_message>, <gate_name>]",
                "<gate_color>: the color for the light beam and text",
                "<gate_name>: the name for waypoint's name when the gate is going to spawn",
                "<main_message>/<sub_message>: result the prompt message like: <main_message> at: x y z, <sub_message>")
                .defineList("gates", List.of(
                        List.of("gateways:basic/blaze", "#FF8C00", "", "", "Blaze Gate"),
                        List.of("gateways:basic/enderman", "#800080", "", "", "Enderman Gate"),
                        List.of("gateways:basic/slime", "#32CD32", "", "", "Slime Gate"),
                        List.of("gateways:emerald_grove", "#50C878", "", "", "Emerald Grove"),
                        List.of("gateways:endless/blaze", "#FF8C00", "", "", "Endless Blaze Gate"),
                        List.of("gateways:hellish_fortress", "#B22222", "", "", "Hellish Fortress"),
                        List.of("gateways:overworldian_nights", "#1E90FF", "", "", "Overworldian Nights")
                ), obj -> obj instanceof List && ((List<?>) obj).stream().allMatch(e -> e instanceof String));

        SPEC = BUILDER.build();
    }
}