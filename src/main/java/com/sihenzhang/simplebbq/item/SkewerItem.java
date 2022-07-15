package com.sihenzhang.simplebbq.item;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class SkewerItem extends Item {
    private final int duration;

    public SkewerItem(Builder builder) {
        super(new Properties().tab(SimpleBBQ.TAB).food(builder.foodBuilder.build()));
        this.duration = builder.duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Item baseItem) {
        return new Builder(baseItem, 0, 0.0F);
    }

    public static Builder builder(Item baseItem, int nutritionModifier) {
        return new Builder(baseItem, nutritionModifier, 0.0F);
    }

    public static Builder builder(Item baseItem, float saturationModifier) {
        return new Builder(baseItem, 0, saturationModifier);
    }

    public static Builder builder(Item baseItem, int nutritionModifier, float saturationModifier) {
        return new Builder(baseItem, nutritionModifier, saturationModifier);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return duration;
    }

    public static class Builder {
        private FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
        private int duration = 28;

        public Builder() {
        }

        public Builder(Item baseItem, int nutritionModifier, float saturationModifier) {
            var baseFoodProperties = baseItem.getFoodProperties(baseItem.getDefaultInstance(), null);
            if (baseFoodProperties != null) {
                this.foodBuilder = this.foodBuilder.nutrition(baseFoodProperties.getNutrition() + nutritionModifier).saturationMod(baseFoodProperties.getSaturationModifier() + saturationModifier);
                if (baseFoodProperties.isMeat()) {
                    this.foodBuilder = this.foodBuilder.meat();
                }
                if (baseFoodProperties.canAlwaysEat()) {
                    this.foodBuilder = this.foodBuilder.alwaysEat();
                }
                if (baseFoodProperties.isFastFood()) {
                    this.duration = 16;
                }
                if (!baseFoodProperties.getEffects().isEmpty()) {
                    baseFoodProperties.getEffects().forEach(e -> this.foodBuilder = this.foodBuilder.effect(e::getFirst, e.getSecond()));
                }
            }
        }

        public Builder nutrition(int nutrition) {
            foodBuilder = foodBuilder.nutrition(nutrition);
            return this;
        }

        public Builder saturationMod(float saturationModifier) {
            foodBuilder = foodBuilder.saturationMod(saturationModifier);
            return this;
        }

        public Builder meat() {
            foodBuilder = foodBuilder.meat();
            return this;
        }

        public Builder alwaysEat() {
            foodBuilder = foodBuilder.alwaysEat();
            return this;
        }

        public Builder duration(int useDuration) {
            duration = useDuration;
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            foodBuilder = foodBuilder.effect(effect, probability);
            return this;
        }

        public SkewerItem build() {
            return new SkewerItem(this);
        }
    }
}
