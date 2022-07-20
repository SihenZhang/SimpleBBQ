package com.sihenzhang.simplebbq.event;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class PlayerEatingFoodEvent {
    @SubscribeEvent
    public static void onItemUseStart(final LivingEntityUseItemEvent.Start event) {
        var stack = event.getItem();
        var seasoningTag = stack.getTagElement("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (hasSeasoning(seasoningList, "chili_powder")) {
                event.setDuration(Math.max(event.getDuration() - 4, 1));
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(final LivingEntityUseItemEvent.Finish event) {
        var stack = event.getItem();
        var seasoningTag = stack.getTagElement("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (hasSeasoning(seasoningList, "honey")) {
                event.getEntityLiving().heal(2.0F);
            }
            if (event.getEntityLiving() instanceof Player player && !(player instanceof FakePlayer)) {
                var foodProperties = stack.getFoodProperties(player);
                if (foodProperties != null) {
                    var foodData = player.getFoodData();
                    var baseNutrition = foodProperties.getNutrition();
                    var baseSaturationModifier = foodProperties.getSaturationModifier();
                    var additionalNutrition = 0;
                    var additionalSaturationModifier = 0.0F;
                    if (hasSeasoning(seasoningList, "salt_and_pepper")) {
                        additionalNutrition += 1;
                    }
                    if (hasSeasoning(seasoningList, "cumin")) {
                        additionalSaturationModifier += 0.1F;
                    }
                    foodData.setFoodLevel(Mth.clamp(foodData.getFoodLevel() + additionalNutrition, 0, 20));
                    foodData.setSaturation(Math.min(foodData.getSaturationLevel() + baseNutrition * additionalSaturationModifier * 2.0F + additionalNutrition * (baseSaturationModifier + additionalSaturationModifier) * 2.0F, (float) foodData.getFoodLevel()));
                }
            }
        }
    }

    private static boolean hasSeasoning(ListTag seasoning, String name) {
        return seasoning.stream().filter(tag -> tag.getId() == Tag.TAG_STRING).map(StringTag.class::cast).anyMatch(tag -> tag.getAsString().equalsIgnoreCase(name));
    }
}
