package com.sihenzhang.simplebbq.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static ItemStack convertToItemStack(JsonElement json, String memberName) {
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            return ShapedRecipe.itemStackFromJson(object);
        } else if (GsonHelper.isStringValue(json)) {
            return GsonHelper.convertToItem(json, memberName).getDefaultInstance();
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item stack, was " + GsonHelper.getType(json));
        }
    }

    public static ItemStack getAsItemStack(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return convertToItemStack(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item stack");
        }
    }
}
