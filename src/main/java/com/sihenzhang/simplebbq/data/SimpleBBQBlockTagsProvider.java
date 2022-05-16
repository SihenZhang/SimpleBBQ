package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SimpleBBQBlockTagsProvider extends BlockTagsProvider {
    public SimpleBBQBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SimpleBBQRegistry.GRILL_BLOCK.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(SimpleBBQRegistry.GRILL_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get());
    }

    @Override
    public String getName() {
        return "SimpleBBQ Block Tags";
    }
}
