package com.sihenzhang.simplebbq.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.JsonUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
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

public class SkeweringRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack result;

    public SkeweringRecipe(ResourceLocation id, Ingredient ingredient, int count, ItemStack result) {
        Preconditions.checkArgument(count >= 1 && count <= 64, "Count must be between 1 and 64");
        this.id = id;
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        var stack = pContainer.getItem(0);
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    public int getCount() {
        return count;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.SKEWERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SimpleBBQRegistry.SKEWERING_RECIPE_TYPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SkeweringRecipe> {
        @Override
        public SkeweringRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            var result = JsonUtils.getAsItemStack(pSerializedRecipe, "result");
            if (GsonHelper.isObjectNode(pSerializedRecipe, "ingredient")) {
                var ingredientObject = GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient");
                if (ingredientObject.has("ingredient")) {
                    var ingredient = JsonUtils.getAsIngredient(ingredientObject, "ingredient");
                    var count = GsonHelper.getAsInt(ingredientObject, "count", 1);
                    return new SkeweringRecipe(pRecipeId, ingredient, count, result);
                }
            }
            var ingredient = JsonUtils.getAsIngredient(pSerializedRecipe, "ingredient");
            return new SkeweringRecipe(pRecipeId, ingredient, 1, result);
        }

        @Nullable
        @Override
        public SkeweringRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var count = pBuffer.readVarInt();
            var result = pBuffer.readItem();
            return new SkeweringRecipe(pRecipeId, ingredient, count, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SkeweringRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeVarInt(pRecipe.count);
            pBuffer.writeItem(pRecipe.result);
        }
    }
}
