package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
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

        this.simpleItem(SimpleBBQRegistry.RAW_SKEWERED_BEEF.get(), RLUtils.createRL("item/raw_skewered_beef"));
        this.simpleItem(SimpleBBQRegistry.COOKED_SKEWERED_BEEF.get(), RLUtils.createRL("item/cooked_skewered_beef"));
        this.simpleItem(SimpleBBQRegistry.RAW_SKEWERED_CHICKEN.get(), RLUtils.createRL("item/raw_skewered_chicken"));
        this.simpleItem(SimpleBBQRegistry.COOKED_SKEWERED_CHICKEN.get(), RLUtils.createRL("item/cooked_skewered_chicken"));
        this.simpleItem(SimpleBBQRegistry.RAW_SKEWERED_PORK.get(), RLUtils.createRL("item/raw_skewered_pork"));
        this.simpleItem(SimpleBBQRegistry.COOKED_SKEWERED_PORK.get(), RLUtils.createRL("item/cooked_skewered_pork"));
        this.simpleItem(SimpleBBQRegistry.RAW_SKEWERED_COD.get(), RLUtils.createRL("item/raw_skewered_cod"));
        this.simpleItem(SimpleBBQRegistry.COOKED_SKEWERED_COD.get(), RLUtils.createRL("item/cooked_skewered_cod"));
        this.simpleItem(SimpleBBQRegistry.RAW_SKEWERED_SALMON.get(), RLUtils.createRL("item/raw_skewered_salmon"));
        this.simpleItem(SimpleBBQRegistry.COOKED_SKEWERED_SALMON.get(), RLUtils.createRL("item/cooked_skewered_salmon"));
    }

    public ItemModelBuilder blockItem(Block block) {
        return this.blockItem(block, RLUtils.createRL("block/" + getBlockName(block)));
    }

    public ItemModelBuilder blockItem(Block block, ResourceLocation model) {
        return this.withExistingParent(getBlockName(block), model);
    }

    public ItemModelBuilder simpleItem(ItemLike item, ResourceLocation texture) {
        return this.item(getItemName(item), texture);
    }

    public ItemModelBuilder item(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.createVanillaRL("item/generated"), "layer0", texture);
    }

    protected static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    protected static String getItemName(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }
}
