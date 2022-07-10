package com.sihenzhang.simplebbq;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.item.GrillItem;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SimpleBBQRecipeType;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
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
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleBBQ.MOD_ID);

    public static final RegistryObject<RecipeType<GrillCookingRecipe>> GRILL_COOKING_RECIPE_TYPE = RECIPE_TYPES.register("grill_cooking", () -> new SimpleBBQRecipeType<>("grill_cooking"));
    public static final RegistryObject<RecipeSerializer<GrillCookingRecipe>> GRILL_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("grill_cooking", GrillCookingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<SkeweringRecipe>> SKEWERING_RECIPE_TYPE = RECIPE_TYPES.register("skewering", () -> new SimpleBBQRecipeType<>("skewering"));
    public static final RegistryObject<RecipeSerializer<SkeweringRecipe>> SKEWERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("skewering", SkeweringRecipe.Serializer::new);

    public static final RegistryObject<SimpleParticleType> CAMPFIRE_SMOKE_UNDER_GRILL = PARTICLE_TYPES.register("campfire_smoke_under_grill", () -> new SimpleParticleType(true));

    public static final RegistryObject<Block> GRILL_BLOCK = BLOCKS.register("grill", GrillBlock::new);
    public static final RegistryObject<Item> GRILL_BLOCK_ITEM = ITEMS.register("grill", GrillItem::new);
    public static final RegistryObject<BlockEntityType<GrillBlockEntity>> GRILL_BLOCK_ENTITY = BLOCK_ENTITIES.register("grill", () -> BlockEntityType.Builder.of(GrillBlockEntity::new, GRILL_BLOCK.get()).build(null));
    public static final RegistryObject<Block> SKEWERING_TABLE_BLOCK = BLOCKS.register("skewering_table", SkeweringTableBlock::new);
    public static final RegistryObject<Item> SKEWERING_TABLE_BLOCK_ITEM = ITEMS.register("skewering_table", () -> new BlockItem(SKEWERING_TABLE_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<BlockEntityType<SkeweringTableBlockEntity>> SKEWERING_TABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("skewering_table", () -> BlockEntityType.Builder.of(SkeweringTableBlockEntity::new, SKEWERING_TABLE_BLOCK.get()).build(null));

    public static final RegistryObject<PoiType> SKEWERMAN_POI = POI_TYPES.register("skewerman", () -> new PoiType("skewerman", PoiType.getBlockStates(SKEWERING_TABLE_BLOCK.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> SKEWERMAN = PROFESSIONS.register("skewerman", () -> new VillagerProfession("skewerman", SKEWERMAN_POI.get(), ImmutableSet.of(), ImmutableSet.of(), null));

    public static final RegistryObject<Item> CHILI_POWDER = ITEMS.register("chili_powder", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> CUMIN = ITEMS.register("cumin", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> SALT_AND_PEPPER = ITEMS.register("salt_and_pepper", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));

    public static final RegistryObject<Item> BEEF_SKEWER = ITEMS.register("beef_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_BEEF_SKEWER = ITEMS.register("cooked_beef_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> CHICKEN_SKEWER = ITEMS.register("chicken_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_CHICKEN_SKEWER = ITEMS.register("cooked_chicken_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> MUTTON_SKEWER = ITEMS.register("mutton_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_MUTTON_SKEWER = ITEMS.register("cooked_mutton_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> PORK_SKEWER = ITEMS.register("pork_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_PORK_SKEWER = ITEMS.register("cooked_pork_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> RABBIT_SKEWER = ITEMS.register("rabbit_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_RABBIT_SKEWER = ITEMS.register("cooked_rabbit_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COD_SKEWER = ITEMS.register("cod_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_COD_SKEWER = ITEMS.register("cooked_cod_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> SALMON_SKEWER = ITEMS.register("salmon_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> COOKED_SALMON_SKEWER = ITEMS.register("cooked_salmon_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> BREAD_SLICE_SKEWER = ITEMS.register("bread_slice_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> TOAST_SKEWER = ITEMS.register("toast_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> MUSHROOM_SKEWER = ITEMS.register("mushroom_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> ROASTED_MUSHROOM_SKEWER = ITEMS.register("roasted_mushroom_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> POTATO_SKEWER = ITEMS.register("potato_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
    public static final RegistryObject<Item> BAKED_POTATO_SKEWER = ITEMS.register("baked_potato_skewer", () -> new Item(new Item.Properties().tab(SimpleBBQ.TAB)));
}
