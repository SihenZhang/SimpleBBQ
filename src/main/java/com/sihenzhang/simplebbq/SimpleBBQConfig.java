package com.sihenzhang.simplebbq;

import net.minecraftforge.common.ForgeConfigSpec;

public class SimpleBBQConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER;

    static {
        final ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();

        commonBuilder.comment("General settings").push("general");
        CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER = commonBuilder
                .comment("Set this value to change the speed modifier of the cooking time for Campfire Cooking Recipe when cooking on the grill.\nactualCookingTime = cookingTime * campfireCookingOnGrillSpeedModifier")
                .worldRestart()
                .defineInRange("campfireCookingOnGrillSpeedModifier", 0.5, 0.0, 1.0);

        COMMON_CONFIG = commonBuilder.build();
    }
}