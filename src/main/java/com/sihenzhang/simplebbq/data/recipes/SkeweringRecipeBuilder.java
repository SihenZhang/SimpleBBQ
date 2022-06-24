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
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SkeweringRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final Ingredient ingredient;
    private final int ingredientCount;

    public SkeweringRecipeBuilder(ItemLike result, int resultCount, Ingredient ingredient, int ingredientCount) {
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, int ingredientCount, ItemLike result, int resultCount) {
        return new SkeweringRecipeBuilder(result, resultCount, ingredient, ingredientCount);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, int ingredientCount, ItemLike result) {
        return skewering(ingredient, ingredientCount, result, 1);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, ItemLike result, int resultCount) {
        return skewering(ingredient, 1, result, resultCount);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, ItemLike result) {
        return skewering(ingredient, 1, result, 1);
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
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, ingredient, ingredientCount, result, resultCount));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final int ingredientCount;
        private final Item result;
        private final int resultCount;

        public Result(ResourceLocation id, Ingredient ingredient, int ingredientCount, Item result, int resultCount) {
            this.id = id;
            this.ingredient = ingredient;
            this.ingredientCount = ingredientCount;
            this.result = result;
            this.resultCount = resultCount;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            if (ingredientCount > 1) {
                var ingredientWithCount = new JsonObject();
                ingredientWithCount.add("ingredient", ingredient.toJson());
                ingredientWithCount.addProperty("count", ingredientCount);
                pJson.add("ingredient", ingredientWithCount);
            } else {
                pJson.add("ingredient", ingredient.toJson());
            }
            var resultObject = new JsonObject();
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
            if (resultCount > 1) {
                resultObject.addProperty("count", resultCount);
            }
            pJson.add("result", resultObject);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SimpleBBQRegistry.SKEWERING_RECIPE_SERIALIZER.get();
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
