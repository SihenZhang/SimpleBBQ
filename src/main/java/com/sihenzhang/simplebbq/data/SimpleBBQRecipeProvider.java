package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SkeweringRecipeBuilder;
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

        // skewering recipe
        skeweringRecipe(pFinishedRecipeConsumer, Items.BEEF, SimpleBBQRegistry.RAW_SKEWERED_BEEF_ITEM.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.PORKCHOP, SimpleBBQRegistry.RAW_SKEWERED_PORK_ITEM.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.COD, SimpleBBQRegistry.RAW_SKEWERED_COD_ITEM.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.SALMON, SimpleBBQRegistry.RAW_SKEWERED_SALMON_ITEM.get());

        // skewer cooking recipe
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_BEEF_ITEM.get(), SimpleBBQRegistry.COOKED_SKEWERED_BEEF_ITEM.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_PORK_ITEM.get(), SimpleBBQRegistry.COOKED_SKEWERED_PORK_ITEM.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_COD_ITEM.get(), SimpleBBQRegistry.COOKED_SKEWERED_COD_ITEM.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_SALMON_ITEM.get(), SimpleBBQRegistry.COOKED_SKEWERED_SALMON_ITEM.get(), 400);
    }

    protected static void grillCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pCookingTime) {
        GrillCookingRecipeBuilder.cooking(Ingredient.of(pIngredient), pResult, pCookingTime).save(pFinishedRecipeConsumer, getSimpleRecipeName("grill_cooking", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, int pIngredientCount, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult, pResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, int pIngredientCount, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult, pResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static String getSimpleRecipeName(ItemLike pItemLike) {
        return SimpleBBQ.MOD_ID + ":" + getItemName(pItemLike);
    }

    protected static String getSimpleRecipeName(String pRecipeType, ItemLike pItemLike) {
        return SimpleBBQ.MOD_ID + ":" + pRecipeType + "/" + getItemName(pItemLike);
    }

    @Override
    public String getName() {
        return "SimpleBBQ Recipes";
    }
}
