package com.sihenzhang.simplebbq.integration.jei;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final String MOD_ID = "jei";
    public static final ResourceLocation RECIPE_GUI_VANILLA = RLUtils.createRL(MOD_ID, "textures/gui/gui_vanilla.png");

    @Override
    public ResourceLocation getPluginUid() {
        return RLUtils.createRL("simple_bbq");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new GrillCookingCategory(guiHelper));
        registration.addRecipeCategories(new SkeweringCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(GrillCookingCategory.GRILL_COOKING_RECIPE_TYPE, recipeManager.getAllRecipesFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get()));
        registration.addRecipes(SkeweringCategory.SKEWERING_RECIPE_TYPE, recipeManager.getAllRecipesFor(SimpleBBQRegistry.SKEWERING_RECIPE_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(), GrillCookingCategory.GRILL_COOKING_RECIPE_TYPE);
        registration.addRecipeCatalyst(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.get().getDefaultInstance(), SkeweringCategory.SKEWERING_RECIPE_TYPE);
    }
}
