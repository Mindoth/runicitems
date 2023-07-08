package net.mindoth.runicitems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RunicItemsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> BARRAGE_POWER_SCALE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FREEZE_AI;

    static {
        BUILDER.push("Configs for Runic Items");

        BARRAGE_POWER_SCALE = BUILDER.comment("By how much should the Barrage spells' power be multiplied (Default = 1)")
                .define("Barrage Power Multiplier", 1);

        FREEZE_AI = BUILDER.comment("Should Ice Barrage freeze mobs' AI? Wont work on Ender Dragon, Wither or Elder Guardian (Default = false)")
                .define("Barrage Freeze AI", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
