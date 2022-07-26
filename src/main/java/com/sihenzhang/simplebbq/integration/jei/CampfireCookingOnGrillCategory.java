package com.sihenzhang.simplebbq.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQConfig;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;

public class CampfireCookingOnGrillCategory extends AbstractCookingWithoutFuelAndXpCategory<CampfireCookingRecipe> {
    public static final RecipeType<CampfireCookingRecipe> RECIPE_TYPE = RecipeType.create(SimpleBBQ.MOD_ID, "campfire_cooking_on_grill", CampfireCookingRecipe.class);

    public CampfireCookingOnGrillCategory(IGuiHelper guiHelper) {
        super(guiHelper, new DrawableDoubleItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(), Items.CAMPFIRE.getDefaultInstance()), "category.campfire_cooking_on_grill", 400);
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends CampfireCookingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    protected IDrawableAnimated getArrow(int cookingTime) {
        return super.getArrow(Math.max((int) (cookingTime * SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER.get()), 1));
    }

    @Override
    protected void drawCookingTime(int cookingTime, PoseStack stack) {
        super.drawCookingTime(Math.max((int) (cookingTime * SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_SPEED_MODIFIER.get()), 1), stack);
    }
}
