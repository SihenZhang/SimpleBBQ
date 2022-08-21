package com.sihenzhang.simplebbq.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.util.I18nUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SeasoningCategory implements IRecipeCategory<SeasoningRecipe> {
    public static final RecipeType<SeasoningRecipe> RECIPE_TYPE = RecipeType.create(SimpleBBQ.MOD_ID, "seasoning", SeasoningRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<SeasoningRecipe, List<ItemStack>> cachedResultItems;

    public SeasoningCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(ModIntegrationJei.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build();
        this.icon = new DrawableDoubleItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(), SimpleBBQRegistry.CHILI_POWDER.get().getDefaultInstance());
        this.cachedResultItems = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<>() {
            @Override
            public List<ItemStack> load(SeasoningRecipe key) {
                return Arrays.stream(key.getIngredient().getItems()).map(stack -> {
                    var copiedStack = stack.copy();
                    var seasoningTag = copiedStack.getOrCreateTagElement("Seasoning");
                    var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
                    seasoningList.add(StringTag.valueOf(key.getName().toLowerCase(Locale.ROOT)));
                    // Sort the seasoning list so that item can be stacked even if the seasoning order is not the same
                    seasoningList.sort(Comparator.comparing(Tag::getAsString));
                    seasoningTag.put("SeasoningList", seasoningList);
                    seasoningTag.putBoolean("HasEffect", true);
                    return copiedStack;
                }).toList();
            }
        });
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends SeasoningRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<SeasoningRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "category.seasoning");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SeasoningRecipe recipe, IFocusGroup focuses) {
        var inputItems = List.of(recipe.getIngredient().getItems());
        if (inputItems.stream().anyMatch(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.OUTPUT).anyMatch(focus -> stack.sameItem(focus.getTypedValue().getIngredient())))) {
            inputItems = inputItems.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.OUTPUT).anyMatch(focus -> stack.sameItem(focus.getTypedValue().getIngredient()))).toList();
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(inputItems);
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(recipe.getSeasoning());
        var resultItems = cachedResultItems.getUnchecked(recipe);
        if (resultItems.stream().anyMatch(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT).anyMatch(focus -> stack.sameItem(focus.getTypedValue().getIngredient())))) {
            resultItems = resultItems.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT).anyMatch(focus -> stack.sameItem(focus.getTypedValue().getIngredient()))).toList();
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStacks(resultItems);
    }
}
