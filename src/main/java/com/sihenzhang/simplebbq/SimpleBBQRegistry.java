package com.sihenzhang.simplebbq;

import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.item.GrillItem;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SimpleBBQRecipeType;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
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
    public static final RegistryObject<RecipeType<SkeweringRecipe>> SKEWERING_RECIPE_TYPE = RECIPE_TYPES.register("skewering", () -> new SimpleBBQRecipeType<>("skewering"));
    public static final RegistryObject<RecipeSerializer<SkeweringRecipe>> SKEWERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("skewering", SkeweringRecipe.Serializer::new);

    public static final RegistryObject<Block> GRILL_BLOCK = BLOCKS.register("grill", GrillBlock::new);
    public static final RegistryObject<Item> GRILL_BLOCK_ITEM = ITEMS.register("grill", GrillItem::new);
    public static final RegistryObject<BlockEntityType<GrillBlockEntity>> GRILL_BLOCK_ENTITY = BLOCK_ENTITIES.register("grill", () -> BlockEntityType.Builder.of(GrillBlockEntity::new, GRILL_BLOCK.get()).build(null));
    public static final RegistryObject<Block> SKEWERING_TABLE_BLOCK = BLOCKS.register("skewering_table", SkeweringTableBlock::new);
    public static final RegistryObject<Item> SKEWERING_TABLE_BLOCK_ITEM = ITEMS.register("skewering_table", () -> new BlockItem(SKEWERING_TABLE_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<BlockEntityType<SkeweringTableBlockEntity>> SKEWERING_TABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("skewering_table", () -> BlockEntityType.Builder.of(SkeweringTableBlockEntity::new, SKEWERING_TABLE_BLOCK.get()).build(null));

//    public static final RegistryObject<Item> SKEWERED_BEEF_RAW_ITEM = ITEMS.register("skewered_beef_raw", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
}
