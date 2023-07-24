package net.mindoth.runicitems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RunicItemsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Float> ARCHERBOOTS_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> FIGHTERBOOTS_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WIZARDBOOTS_TRADE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARCHERBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> FIGHTERBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> WIZARDBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> EAGLEBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> WARRIORBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> SORCERERBOOTS_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Float> TABLET_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> MALLET_CHANCE;

    static {
        BUILDER.push("Configs for Runic Items");

        ARCHERBOOTS_CHANCE = BUILDER.comment("Chance of finding Archer Boots in a buried treasure (Default = 0.2 = 20%)")
                .define("Archer Boots Chance", 0.2f);

        FIGHTERBOOTS_CHANCE = BUILDER.comment("Chance of getting Fighter Boots as a drop from a Ravager (Default = 0.25 = 25%)")
                .define("Fighter Boots Chance", 0.25f);

        WIZARDBOOTS_TRADE = BUILDER.comment("Should the Cleric Villager be able to trade you Wizard Boots? (Default = true)")
                .define("Wizard Boots Trade", true);

        ARCHERBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Archer Boots (Default = 3)")
                .define("Archer Boots Bonus", 3);

        FIGHTERBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Fighter Boots (Default = 3)")
                .define("Fighter Boots Bonus", 3);

        WIZARDBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Wizard Boots (Default = 3)")
                .define("Wizard Boots Bonus", 3);

        EAGLEBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Eagle Boots (Default = 6)")
                .define("Eagle Boots Bonus", 6);

        WARRIORBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Warrior Boots (Default = 6)")
                .define("Warrior Boots Bonus", 6);

        SORCERERBOOTS_BONUS = BUILDER.comment("Bonus Damage given by Sorcerer Boots (Default = 6)")
                .define("Sorcerer Boots Bonus", 6);

        TABLET_CHANCE = BUILDER.comment("Chance of getting the Secrets of Life and Death as a drop from an Evoker instead of a Totem of Undying (Default = 0.1 = 10%)")
                .define("Tablet Chance", 0.1f);

        MALLET_CHANCE = BUILDER.comment("Chance of a Wither Skeleton to spawn holding a Mallet (Default = 0.15 = 15%)")
                .define("Mallet Chance", 0.15f);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
