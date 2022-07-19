package com.sihenzhang.simplebbq.event;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class PlayerEatingFoodEvent {
    @SubscribeEvent
    public static void onItemUseStart(final LivingEntityUseItemEvent.Start event) {
        if (event.getEntityLiving() instanceof Player player && !(player instanceof FakePlayer)) {
            var stack = event.getItem();
            var seasoningTag = stack.getTagElement("Seasoning");
            if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
                var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
                if (seasoningList.contains(StringTag.valueOf("chili_powder"))) {
                    event.setDuration(Math.max(event.getDuration() - 4, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(final LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof Player player && !(player instanceof FakePlayer)) {
            var stack = event.getItem();
            var seasoningTag = stack.getTagElement("Seasoning");
            if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
                var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
                var foodProperties = stack.getFoodProperties(player);
                if (foodProperties != null) {
                    var nutrition = foodProperties.getNutrition();
                    var foodData = player.getFoodData();
                    if (seasoningList.contains(StringTag.valueOf("salt_and_pepper"))) {
                        nutrition += 1;
                        foodData.eat(1, 0.0F);
                    }
                    if (seasoningList.contains(StringTag.valueOf("cumin"))) {
                        var foodLevel = foodData.getFoodLevel();
                        var saturationLevel = foodData.getSaturationLevel();
                        foodData.setSaturation(Math.min(saturationLevel + nutrition * 0.2F, (float) foodLevel));
                    }
                }
            }
        }
    }
}
