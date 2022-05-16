package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class SimpleBBQRecipeProvider extends RecipeProvider {
    public SimpleBBQRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        // vanilla cooking recipe
        grillCookingRecipe(pFinishedRecipeConsumer, Items.BEEF, Items.COOKED_BEEF, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.CHICKEN, Items.COOKED_CHICKEN, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.COD, Items.COOKED_COD, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.KELP, Items.DRIED_KELP, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.SALMON, Items.COOKED_SALMON, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.MUTTON, Items.COOKED_MUTTON, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.PORKCHOP, Items.COOKED_PORKCHOP, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.POTATO, Items.BAKED_POTATO, 400);
        grillCookingRecipe(pFinishedRecipeConsumer, Items.RABBIT, Items.COOKED_RABBIT, 400);
    }

    protected static void grillCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pCookingTime) {
        GrillCookingRecipeBuilder.cooking(Ingredient.of(pIngredient), pResult, pCookingTime).save(pFinishedRecipeConsumer, getSimpleRecipeName(pResult) + "_from_grill_cooking");
    }

    protected static String getSimpleRecipeName(ItemLike pItemLike) {
        return SimpleBBQ.MOD_ID + ":" + getItemName(pItemLike);
    }

    @Override
    public String getName() {
        return "SimpleBBQ Recipes";
    }
}
