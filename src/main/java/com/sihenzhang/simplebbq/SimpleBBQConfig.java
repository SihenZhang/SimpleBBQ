package com.sihenzhang.simplebbq;

import net.minecraftforge.common.ForgeConfigSpec;

public class SimpleBBQConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER;

    static {
        final var commonBuilder = new ForgeConfigSpec.Builder();

        commonBuilder.comment("General settings").push("general");
        CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER = commonBuilder
                .comment("Set this value to change the speed modifier of the cooking time for Campfire Cooking Recipe when cooking on the grill.\nactualCookingTime = cookingTime * campfireCookingOnGrillSpeedModifier")
                .worldRestart()
                .defineInRange("campfireCookingOnGrillSpeedModifier", 0.5D, 0.0D, 1.0D);

        COMMON_CONFIG = commonBuilder.build();
    }
}
