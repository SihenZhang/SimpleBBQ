package com.sihenzhang.simplebbq.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.util.I18nUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GrillCookingCategory implements IRecipeCategory<GrillCookingRecipe> {
    public static final RecipeType<GrillCookingRecipe> GRILL_COOKING_RECIPE_TYPE = RecipeType.create(SimpleBBQ.MOD_ID, "grill_cooking", GrillCookingRecipe.class);
    private static final int DEFAULT_COOKING_TIME = 400;

    private final IDrawableAnimated animatedFlame;
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public GrillCookingCategory(IGuiHelper guiHelper) {
        this.animatedFlame = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(ModIntegrationJei.RECIPE_GUI_VANILLA, 82, 114, 14, 14), 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.drawableBuilder(ModIntegrationJei.RECIPE_GUI_VANILLA, 0, 186, 82, 34).addPadding(0, 10, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance());
        this.cachedArrows = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<>() {
            @Override
            public IDrawableAnimated load(Integer cookTime) {
                return guiHelper.drawableBuilder(ModIntegrationJei.RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    protected IDrawableAnimated getArrow(GrillCookingRecipe recipe) {
        var cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = DEFAULT_COOKING_TIME;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends GrillCookingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<GrillCookingRecipe> getRecipeType() {
        return GRILL_COOKING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createComponent("integration", ModIntegrationJei.MOD_ID + ".grill_cooking");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrillCookingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(GrillCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        animatedFlame.draw(stack, 1, 20);
        this.getArrow(recipe).draw(stack, 24, 8);

        var cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            var cookTimeSeconds = cookTime / 20;
            var timeString = I18nUtils.createComponent("gui", ModIntegrationJei.MOD_ID, "category.smelting.time.seconds", cookTimeSeconds);
            var fontRenderer = Minecraft.getInstance().font;
            var stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(stack, timeString, background.getWidth() - stringWidth, 35, 0xFF808080);
        }
    }
}
