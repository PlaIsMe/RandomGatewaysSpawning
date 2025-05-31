package com.pla.plagatesummon;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> GATES;
    public static ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;
    public static ForgeConfigSpec.ConfigValue<Boolean> AUTO_CLAIM;
    public static ForgeConfigSpec.ConfigValue<Boolean> AUTO_UNCLAIM;

    static {
        DEBUG_MODE = BUILDER.comment("Turn on debug mode").define("debug_mode", true);
        AUTO_CLAIM = BUILDER.comment("Auto claiming chunk").define("auto_claim", true);
        AUTO_UNCLAIM = BUILDER.comment("Auto un clamming chunk").define("auto_unclaim", true);
        GATES = BUILDER.comment(
                "A list of gates to be spawned with attributes",
                "Format: [<gate_id>, <gate_color>, <main_message>, <sub_message>, <gate_name>]",
                "<gate_color>: the color for the light beam and text",
                "<gate_name>: the name for waypoint's name when the gate is going to spawn",
                "<main_message>/<sub_message>: result the prompt message like: <main_message> at: x y z, <sub_message>")
                .defineList("gates", List.of(
                        List.of("gateways:blaze_gate", "#FF8C00", "", "", "Blaze Gate"),
                        List.of("gateways:blaze_gate_large", "#FF8C00", "", "", "Blaze Gate Large"),
                        List.of("gateways:blaze_gate_small", "#FF8C00", "", "", "Blaze Gate Small"),
                        List.of("gateways:creeper_gate", "#32CD32", "", "", "Creeper Gate"),
                        List.of("gateways:creeper_gate_large", "#32CD32", "", "", "Creeper Gate Large"),
                        List.of("gateways:creeper_gate_small", "#32CD32", "", "", "Creeper Gate Small"),
                        List.of("gateways:enderman_gate", "#800080", "", "", "Enderman Gate"),
                        List.of("gateways:enderman_gate_large", "#800080", "", "", "Enderman Gate Large"),
                        List.of("gateways:enderman_gate_small", "#800080", "", "", "Enderman Gate Small"),
                        List.of("gateways:ghast_gate", "#F5F5F5", "", "", "Ghast Gate"),
                        List.of("gateways:ghast_gate_large", "#F5F5F5", "", "", "Ghast Gate Large"),
                        List.of("gateways:ghast_gate_small", "#F5F5F5", "", "", "Ghast Gate Small"),
                        List.of("gateways:magma_cube_gate", "#B22222", "", "", "Magma Cube Gate"),
                        List.of("gateways:magma_cube_gate_large", "#B22222", "", "", "Magma Cube Gate Large"),
                        List.of("gateways:magma_cube_gate_small", "#B22222", "", "", "Magma Cube Gate Small"),
                        List.of("gateways:shulker_gate", "#9370DB", "", "", "Shulker Gate"),
                        List.of("gateways:shulker_gate_large", "#9370DB", "", "", "Shulker Gate Large"),
                        List.of("gateways:shulker_gate_small", "#9370DB", "", "", "Shulker Gate Small"),
                        List.of("gateways:skeleton_gate", "#D3D3D3", "", "", "Skeleton Gate"),
                        List.of("gateways:skeleton_gate_large", "#D3D3D3", "", "", "Skeleton Gate Large"),
                        List.of("gateways:skeleton_gate_small", "#D3D3D3", "", "", "Skeleton Gate Small"),
                        List.of("gateways:slime_gate", "#00FF00", "", "", "Slime Gate"),
                        List.of("gateways:slime_gate_large", "#00FF00", "", "", "Slime Gate Large"),
                        List.of("gateways:slime_gate_small", "#00FF00", "", "", "Slime Gate Small"),
                        List.of("gateways:spider_gate", "#000000", "", "", "Spider Gate"),
                        List.of("gateways:spider_gate_large", "#000000", "", "", "Spider Gate Large"),
                        List.of("gateways:spider_gate_small", "#000000", "", "", "Spider Gate Small"),
                        List.of("gateways:witch_gate", "#800080", "", "", "Witch Gate"),
                        List.of("gateways:witch_gate_large", "#800080", "", "", "Witch Gate Large"),
                        List.of("gateways:witch_gate_small", "#800080", "", "", "Witch Gate Small"),
                        List.of("gateways:zombie_gate", "#228B22", "", "", "Zombie Gate"),
                        List.of("gateways:zombie_gate_large", "#228B22", "", "", "Zombie Gate Large"),
                        List.of("gateways:zombie_gate_small", "#228B22", "", "", "Zombie Gate Small")
                ), obj -> obj instanceof List && ((List<?>) obj).stream().allMatch(e -> e instanceof String));

        SPEC = BUILDER.build();
    }
}