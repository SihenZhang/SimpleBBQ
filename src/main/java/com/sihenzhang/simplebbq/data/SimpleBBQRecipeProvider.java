package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SeasoningRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SkeweringRecipeBuilder;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
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

        // skewering recipe
        skeweringRecipe(pFinishedRecipeConsumer, Items.BEEF, SimpleBBQRegistry.BEEF_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.CHICKEN, SimpleBBQRegistry.CHICKEN_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.MUTTON, SimpleBBQRegistry.MUTTON_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.PORKCHOP, SimpleBBQRegistry.PORK_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.RABBIT, SimpleBBQRegistry.RABBIT_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.COD, SimpleBBQRegistry.COD_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.SALMON, SimpleBBQRegistry.SALMON_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.BREAD, SimpleBBQRegistry.BREAD_SLICE_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.BROWN_MUSHROOM, SimpleBBQRegistry.MUSHROOM_SKEWER.get());
        skeweringRecipe(pFinishedRecipeConsumer, Items.POTATO, SimpleBBQRegistry.POTATO_SKEWER.get());

        // skewer cooking recipe
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.BEEF_SKEWER.get(), SimpleBBQRegistry.COOKED_BEEF_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.CHICKEN_SKEWER.get(), SimpleBBQRegistry.COOKED_CHICKEN_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.MUTTON_SKEWER.get(), SimpleBBQRegistry.COOKED_MUTTON_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.PORK_SKEWER.get(), SimpleBBQRegistry.COOKED_PORK_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RABBIT_SKEWER.get(), SimpleBBQRegistry.COOKED_RABBIT_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.COD_SKEWER.get(), SimpleBBQRegistry.COOKED_COD_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.SALMON_SKEWER.get(), SimpleBBQRegistry.COOKED_SALMON_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(), SimpleBBQRegistry.TOAST_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.MUSHROOM_SKEWER.get(), SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER.get(), 200);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.POTATO_SKEWER.get(), SimpleBBQRegistry.BAKED_POTATO_SKEWER.get(), 200);

        // seasoning recipe
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_HONEY), Items.HONEY_BOTTLE, "honey");
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CHILI_POWDER), SimpleBBQRegistry.CHILI_POWDER.get());
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CUMIN), SimpleBBQRegistry.CUMIN.get());
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_SALT_AND_PEPPER), SimpleBBQRegistry.SALT_AND_PEPPER.get());
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

    protected static void seasoningRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Ingredient pIngredient, ItemLike pSeasoning) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), getItemName(pSeasoning)).save(pFinishedRecipeConsumer, getSimpleRecipeName("seasoning", getItemName(pSeasoning)));
    }

    protected static void seasoningRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Ingredient pIngredient, ItemLike pSeasoning, String name) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), name).save(pFinishedRecipeConsumer, getSimpleRecipeName("seasoning", name));
    }

    protected static String getSimpleRecipeName(ItemLike pItemLike) {
        return getSimpleRecipeName(getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String name) {
        return SimpleBBQ.MOD_ID + ":" + name;
    }

    protected static String getSimpleRecipeName(String pRecipeType, ItemLike pItemLike) {
        return getSimpleRecipeName(pRecipeType, getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String pRecipeType, String name) {
        return SimpleBBQ.MOD_ID + ":" + pRecipeType + "/" + name;
    }

    @Override
    public String getName() {
        return "SimpleBBQ Recipes";
    }
}
