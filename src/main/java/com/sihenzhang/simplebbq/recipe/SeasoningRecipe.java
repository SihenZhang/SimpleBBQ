package com.sihenzhang.simplebbq.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.JsonUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Locale;

public class SeasoningRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Ingredient seasoning;
    private final String name;

    public SeasoningRecipe(ResourceLocation id, Ingredient ingredient, Ingredient seasoning, String name) {
        this.id = id;
        this.ingredient = ingredient;
        this.seasoning = seasoning;
        this.name = name;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        var inputStack = pContainer.getItem(0);
        var seasoningStack = pContainer.getItem(1);
        if (!ingredient.test(inputStack) || !seasoning.test(seasoningStack)) {
            return false;
        }
        var seasoningTag = inputStack.getTagElement("Seasoning");
        if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            for (var i = 0; i < seasoningList.size(); i++) {
                if (seasoningList.getString(i).equalsIgnoreCase(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        var result = pContainer.getItem(0);
        var seasoningTag = result.getOrCreateTagElement("Seasoning");
        var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
        seasoningList.add(StringTag.valueOf(name.toLowerCase(Locale.ROOT)));
        seasoningTag.put("SeasoningList", seasoningList);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> {
            list.add(ingredient);
            list.add(seasoning);
        });
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.SEASONING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SimpleBBQRegistry.SEASONING_RECIPE_TYPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SeasoningRecipe> {
        @Override
        public SeasoningRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            var ingredient = JsonUtils.getAsIngredient(pSerializedRecipe, "ingredient");
            var seasoning = JsonUtils.getAsIngredient(pSerializedRecipe, "seasoning");
            var name = GsonHelper.getAsString(pSerializedRecipe, "name").toLowerCase(Locale.ROOT);
            return new SeasoningRecipe(pRecipeId, ingredient, seasoning, name);
        }

        @Nullable
        @Override
        public SeasoningRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var seasoning = Ingredient.fromNetwork(pBuffer);
            var name = pBuffer.readUtf().toLowerCase(Locale.ROOT);
            return new SeasoningRecipe(pRecipeId, ingredient, seasoning, name);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SeasoningRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pRecipe.seasoning.toNetwork(pBuffer);
            pBuffer.writeUtf(pRecipe.name);
        }
    }
}
