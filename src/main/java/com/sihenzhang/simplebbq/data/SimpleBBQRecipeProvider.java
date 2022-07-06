package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SkeweringRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SimpleBBQRecipeProvider extends RecipeProvider {
    public SimpleBBQRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        // crafting recipe
        ShapedRecipeBuilder.shaped(SimpleBBQRegistry.GRILL_BLOCK.get())
                .define('#', Blocks.IRON_TRAPDOOR)
                .define('X', Tags.Items.INGOTS_IRON)
                .define('I', Tags.Items.RODS_WOODEN)
                .pattern("X#X")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(pFinishedRecipeConsumer);
        ShapedRecipeBuilder.shaped(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get())
                .define('#', ItemTags.PLANKS)
                .define('_', Blocks.SMOOTH_STONE_SLAB)
                .pattern("__")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_stone_slab", has(Blocks.SMOOTH_STONE_SLAB))
                .save(pFinishedRecipeConsumer);

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
        skeweringRecipe(pFinishedRecipeConsumer, Items.BEEF, SimpleBBQRegistry.RAW_SKEWERED_BEEF.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.CHICKEN, SimpleBBQRegistry.RAW_SKEWERED_CHICKEN.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.MUTTON, SimpleBBQRegistry.RAW_SKEWERED_MUTTON.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.PORKCHOP, SimpleBBQRegistry.RAW_SKEWERED_PORK.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.RABBIT, SimpleBBQRegistry.RAW_SKEWERED_RABBIT.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.COD, SimpleBBQRegistry.RAW_SKEWERED_COD.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.SALMON, SimpleBBQRegistry.RAW_SKEWERED_SALMON.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.POTATO, SimpleBBQRegistry.SKEWERED_POTATO.get());

        // skewer cooking recipe
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_BEEF.get(), SimpleBBQRegistry.COOKED_SKEWERED_BEEF.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_CHICKEN.get(), SimpleBBQRegistry.COOKED_SKEWERED_CHICKEN.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_MUTTON.get(), SimpleBBQRegistry.COOKED_SKEWERED_MUTTON.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_PORK.get(), SimpleBBQRegistry.COOKED_SKEWERED_PORK.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_RABBIT.get(), SimpleBBQRegistry.COOKED_SKEWERED_RABBIT.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_COD.get(), SimpleBBQRegistry.COOKED_SKEWERED_COD.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RAW_SKEWERED_SALMON.get(), SimpleBBQRegistry.COOKED_SKEWERED_SALMON.get(), 400);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.SKEWERED_POTATO.get(), SimpleBBQRegistry.BAKED_SKEWERED_POTATO.get(), 400);
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
