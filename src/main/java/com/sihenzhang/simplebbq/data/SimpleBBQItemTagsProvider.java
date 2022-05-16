package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SimpleBBQItemTagsProvider extends ItemTagsProvider {
    public SimpleBBQItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(SimpleBBQItemTags.SKEWER).addTag(Tags.Items.RODS_WOODEN).add(Items.BAMBOO);
    }

    @Override
    public String getName() {
        return "SimpleBBQ Item Tags";
    }
}
