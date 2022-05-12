package com.sihenzhang.simplebbq.recipe;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SimpleBBQRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    private final String identifier;

    public SimpleBBQRecipeType(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return SimpleBBQ.MOD_ID + ":" + identifier;
    }
}
