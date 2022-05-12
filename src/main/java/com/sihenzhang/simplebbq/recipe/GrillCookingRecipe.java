package com.sihenzhang.simplebbq.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class GrillCookingRecipe extends AbstractCookingRecipe {
    public GrillCookingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, int pCookingTime) {
        super(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), pId, pGroup, pIngredient, pResult, 0, pCookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.GRILL_COOKING_RECIPE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GrillCookingRecipe> {
        @Override
        public GrillCookingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            var group = GsonHelper.getAsString(pSerializedRecipe, "group", "");
            var ingredient = JsonUtils.getAsIngredient(pSerializedRecipe, "ingredient");
            var result = JsonUtils.getAsItemStack(pSerializedRecipe, "result");
            var cookingTime = GsonHelper.getAsInt(pSerializedRecipe, "cookingtime", 100);
            return new GrillCookingRecipe(pRecipeId, group, ingredient, result, cookingTime);
        }

        @Nullable
        @Override
        public GrillCookingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var group = pBuffer.readUtf();
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var result = pBuffer.readItem();
            var cookingTime = pBuffer.readVarInt();
            return new GrillCookingRecipe(pRecipeId, group, ingredient, result, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GrillCookingRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }
    }
}
