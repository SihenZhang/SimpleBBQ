package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleBBQItemModelProvider extends ItemModelProvider {
    public SimpleBBQItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.blockItem(SimpleBBQRegistry.GRILL_BLOCK.get());
        this.blockItem(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get());

        this.simpleItem(SimpleBBQRegistry.CHILI_POWDER.get());
        this.simpleItem(SimpleBBQRegistry.CUMIN.get());
        this.simpleItem(SimpleBBQRegistry.SALT_AND_PEPPER.get());

        this.simpleHandheldItem(SimpleBBQRegistry.BEEF_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_BEEF_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.CHICKEN_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_CHICKEN_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.MUTTON_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_MUTTON_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.PORK_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_PORK_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.RABBIT_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_RABBIT_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COD_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_COD_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.SALMON_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_SALMON_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.BREAD_SLICE_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.TOAST_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.MUSHROOM_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.POTATO_SKEWER.get());
        this.simpleHandheldItem(SimpleBBQRegistry.BAKED_POTATO_SKEWER.get());
    }

    public ItemModelBuilder blockItem(Block block) {
        return this.blockItem(block, RLUtils.createRL("block/" + getBlockName(block)));
    }

    public ItemModelBuilder blockItem(Block block, ResourceLocation model) {
        return this.withExistingParent(getBlockName(block), model);
    }

    public ItemModelBuilder simpleItem(Item item) {
        return this.simpleItem(item, RLUtils.createRL("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleItem(Item item, ResourceLocation texture) {
        return this.item(getItemName(item), texture);
    }

    public ItemModelBuilder item(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.createVanillaRL("item/generated"), "layer0", texture);
    }

    public ItemModelBuilder simpleHandheldItem(Item item) {
        return this.simpleHandheldItem(item, RLUtils.createRL("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleHandheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(getItemName(item), texture);
    }

    public ItemModelBuilder handheldItem(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.createVanillaRL("item/handheld"), "layer0", texture);
    }

    protected static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    protected static String getItemName(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }
}
