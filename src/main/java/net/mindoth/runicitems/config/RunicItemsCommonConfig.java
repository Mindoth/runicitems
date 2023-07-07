package net.mindoth.runicitems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RunicItemsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> BARRAGE_POWER_SCALE;

    static {
        BUILDER.push("Configs for Runic Items");

        BARRAGE_POWER_SCALE = BUILDER.comment("By how much should the Barrage spells' power be multiplied (Default = 1)")
                .define("Barrage Power Multiplier", 1);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
