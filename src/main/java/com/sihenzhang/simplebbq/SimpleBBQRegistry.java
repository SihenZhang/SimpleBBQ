package com.sihenzhang.simplebbq;

import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SimpleBBQRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SimpleBBQRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleBBQ.MOD_ID);

    public static final RegistryObject<RecipeType<GrillCookingRecipe>> GRILL_COOKING_RECIPE_TYPE = RECIPE_TYPES.register("grill_cooking", () -> new SimpleBBQRecipeType<>("grill_cooking"));
    public static final RegistryObject<RecipeSerializer<GrillCookingRecipe>> GRILL_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("grill_cooking", GrillCookingRecipe.Serializer::new);

    public static final RegistryObject<Block> GRILL_BLOCK = BLOCKS.register("grill", GrillBlock::new);
    public static final RegistryObject<Item> GRILL_BLOCK_ITEM = ITEMS.register("grill", () -> new BlockItem(GRILL_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<BlockEntityType<GrillBlockEntity>> GRILL_BLOCK_ENTITY = BLOCK_ENTITIES.register("grill", () -> BlockEntityType.Builder.of(GrillBlockEntity::new, GRILL_BLOCK.get()).build(null));
}
