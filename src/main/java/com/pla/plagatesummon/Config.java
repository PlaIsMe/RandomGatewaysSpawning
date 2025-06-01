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
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate"),
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate"),
                        List.of("pla:herobrine_gate_1", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:herobrine_gate_2", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:bellringer_gate", "#71955B", "The Bellringer will be awakened", "With every toll, the air grows heavier, and your fate draws near. Face it, or be swallowed by the sound of doom!", "Bell Ringer Gate"),
                        List.of("pla:bloodandmadness_gate_1", "#FF0000", "The Blood Boss will emerge", "Its power will grow with every drop spilled. Prepare yourself, or you will be drained dry!", "Blood Gate"),
                        List.of("pla:bloodandmadness_gate_2", "#FF0000", "The Blood Boss will emerge", "Its power will grow with every drop spilled. Prepare yourself, or you will be drained dry!", "Blood Gate"),
                        List.of("pla:captain_gate", "#FF0000", "From the ocean’s depths, her fleet will strike", "Brace yourself, or be swept away!", "Captain Cornelia Gate"),
                        List.of("pla:dame_fortuna_gate", "#71955B", "The Dame Fortuna will be awakened", "Brace yourself, or be swept away!", "Dame Fortuna Gate"),
                        List.of("pla:dread_gate", "#71955B", "The Dread Army will rise and sweep across the world", "Their wrath will leave nothing but ruin in its wake. Stand and fight, or succumb to the darkness!", "Dread Gate"),
                        List.of("pla:drowned_gate", "#00FFFF", "The Drowned Boss will emerge from the abyss", "Its cursed tide will drag the unprepared to a watery grave. Beware the call of the deep!", "Drowned Gate"),
                        List.of("pla:end_gate", "#CC00FA", "Ender pearl party will begin", "Will you join?", "End Gate"),
                        List.of("pla:cursed_armor_gate", "#228B22", "The armors shall awaken", "They seek vengeance for the broken ones you once wore. Can you stand against them?", "Cursed Armor Gate"),
                        List.of("pla:entity_303_gate", "#FFFFFF", "A forbidden force will emerge", "Shadows will spread, and corruption will consume all. Will you face the unknown or flee into the darkness?", "Entity 303 Gate"),
                        List.of("pla:explosion_gate_1", "#83DE71", "The TNT party will begin", "Will you join the chaos or be caught in the blast", "Creeper Gate"),
                        List.of("pla:explosion_gate_2", "#83DE71", "The TNT party will begin", "Will you join the chaos or be caught in the blast", "Creeper Gate"),
                        List.of("pla:fire_dragon_gate", "#FF4500", "The Fire Dragon has been sighted", "Its flames consume everything in its path—stay away unless you wish to be reduced to nothing but embers!", "Fire Dragon Gate"),
                        List.of("pla:ferrous_gate", "#71955B", "The Ferrous Gate will open", "Stand strong, or be forged into the anvil of war!", "Ferrous Gate"),
                        List.of("pla:frostmaw_gate", "#71955B", "The Frost Gate will unleash a frozen terror", "Will you endure or be buried in ice", "Frostmaw Gate"),
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate"),
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate"),
                        List.of("pla:herobrine_gate_5", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:herobrine_gate_6", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:hydra_gate", "#71955B", "The Hydra will awaken", "Its wounds will heal endlessly—only fire will end its reign. Prepare your flame, or be devoured!", "Hydra Gate"),
                        List.of("pla:ice_dragon_gate", "#ADD8E6", "The Ice Dragon has been sighted!", "Its frost will freeze everything in its path—stay away unless you wish to be turned into an unmelting statue of ice!", "Ice Dragon Gate"),
                        List.of("pla:lightning_dragon_gate", "#800080", "The Lighting Dragon has been sighted!", "Thunder will roar, and its strikes will reduce everything to ash—stay away unless you wish to be shattered by the storm!", "Lighting Dragon Gate"),
                        List.of("pla:myrex_gate", "#71955B", "The Myrex Army will march", "Their relentless swarm will devour everything in sight—prepare for battle or be overrun!", "MyRex Gate"),
                        List.of("pla:nameless_king_gate", "#8B0000", "The Nameless King will descend", "His power will shake the earth, and none will escape his wrath. Will you stand or be forgotten?", "Nameless King Gate"),
                        List.of("pla:nether_gate", "#8B0000", "Something will invade from the nether, bringing fire and destruction", "Prepare, or be consumed by the flames!", "Nether Gate"),
                        List.of("pla:orcz_gate", "#004700", "The Orcz Army will shake the land", "Prepare for battle or be trampled!", "Orcz Gate"),
                        List.of("pla:overworld_gate", "#5C54A4", "The Overworld monsters are rising in rebellion", "Chaos will spread—will you restore order or be swept away?", "Overworld Gate"),
                        List.of("pla:raid_gate_1", "#939999", "The Pillagers will invade the world", " Their war horns will echo, and villages will burn. Will you fight back or fall to their conquest?", "Raid Gate"),
                        List.of("pla:raid_gate_2", "#939999", "The Pillagers will invade the world", " Their war horns will echo, and villages will burn. Will you fight back or fall to their conquest?", "Raid Gate"),
                        List.of("pla:raid_gate_3", "#939999", "The Pillagers will invade the world", " Their war horns will echo, and villages will burn. Will you fight back or fall to their conquest?", "Raid Gate"),
                        List.of("pla:raid_gate_special", "#939999", "The Pillager King will lead his army", "His ruthless forces will raid and conquer all in their path. Will you stand against him or watch the world burn?", "Pillager King Gate"),
                        List.of("pla:samurai_gate", "#71955B", "The Samurai Boss will seek challengers", "He will hunt for a worthy opponent—will you face him in battle or be forgotten in defeat?", "Samurai Gate"),
                        List.of("pla:herobrine_gate_3", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:herobrine_gate_4", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:skeleton_gate_1", "#D3D3D3", "A legion of bones will rise", "Will you fight or join the dead?", "Skeleton Gate"),
                        List.of("pla:skeleton_gate_2", "#D3D3D3", "A legion of bones will rise", "Will you fight or join the dead?", "Skeleton Gate"),
                        List.of("pla:swampjaw_gate", "#71955B", "That is no ordinary fish skeleton... The terrifying Swampjaw will take to the skies", "Stay alert, or become its next meal!", "Swampjaw Gate"),
                        List.of("pla:umvuthi_gate", "#FFC000", "The Umvuthi tribe will march", "Their warriors crave battle, and their fury is unstoppable—will you stand or fall", "Umvuthi Gate"),
                        List.of("pla:void_blossom_gate", "#228B22", "Nature's wrath surges through its vines", "Will you soothe its fury or flee for your life", "Void Blossom Gate"),
                        List.of("pla:zombie_gate_1", "#71955B", "The Undead Party rises, and the night belongs to them", "Will you fight or become their feast", "Zombie Gate"),
                        List.of("pla:zombie_gate_2", "#71955B", "The Undead Party rises, and the night belongs to them", "Will you fight or become their feast", "Zombie Gate"),
                        List.of("pla:zombie_gate_special", "#71955B", "A zombie in a red shirt has awakened and will lead a legion of the undead", "Will you strike it down, or be consumed by the horde", "Red-Clad Zombie Gate"),
                        List.of("pla:herobrine_gate_7", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:herobrine_gate_8", "#800080", "Dark forces will rise", "Herobrine will collect every soul he meets. Stay in the light, or be lost to the darkness!", "Herobrine Gate"),
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate"),
                        List.of("pla:apotheosis_gate", "#FF4500", "A powerful force stirs… A Boss Gate will be opened", "Beyond lies an ancient being of immense power. Enter if you dare—but be ready to face your doom!", "Apotheosis Boss Gate")
                ), obj -> obj instanceof List && ((List<?>) obj).stream().allMatch(e -> e instanceof String));


        SPEC = BUILDER.build();
    }
}