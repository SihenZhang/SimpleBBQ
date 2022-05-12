package com.sihenzhang.simplebbq.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GrillCookingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int cookingTime;
    @Nullable
    private String group;

    public GrillCookingRecipeBuilder(ItemLike result, Ingredient ingredient, int cookingTime) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    public static GrillCookingRecipeBuilder cooking(Ingredient ingredient, ItemLike result, int cookingTime) {
        return new GrillCookingRecipeBuilder(result, ingredient, cookingTime);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, group == null ? "" : group, ingredient, result, cookingTime));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final int cookingTime;

        public Result(ResourceLocation id, String group, Ingredient ingredient, Item result, int cookingTime) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.cookingTime = cookingTime;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            if (!group.isEmpty()) {
                pJson.addProperty("group", group);
            }
            pJson.add("ingredient", ingredient.toJson());
            pJson.addProperty("result", ForgeRegistries.ITEMS.getKey(result).toString());
            pJson.addProperty("cookingtime", cookingTime);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SimpleBBQRegistry.GRILL_COOKING_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
