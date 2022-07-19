package com.sihenzhang.simplebbq.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SeasoningRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final Ingredient seasoning;
    private final String name;

    public SeasoningRecipeBuilder(Ingredient ingredient, Ingredient seasoning, String name) {
        this.ingredient = ingredient;
        this.seasoning = seasoning;
        this.name = name;
    }

    public static SeasoningRecipeBuilder seasoning(Ingredient ingredient, Ingredient seasoning, String name) {
        return new SeasoningRecipeBuilder(ingredient, seasoning, name);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new SeasoningRecipeBuilder.Result(pRecipeId, ingredient, seasoning, name));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Ingredient seasoning;
        private final String name;

        public Result(ResourceLocation id, Ingredient ingredient, Ingredient seasoning, String name) {
            this.id = id;
            this.ingredient = ingredient;
            this.seasoning = seasoning;
            this.name = name;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.add("seasoning", seasoning.toJson());
            pJson.addProperty("name", name);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SimpleBBQRegistry.SEASONING_RECIPE_SERIALIZER.get();
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
