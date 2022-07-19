package com.sihenzhang.simplebbq.data;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
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

        var allSkewers = ImmutableSet.of(
                SimpleBBQRegistry.BEEF_SKEWER.get(),
                SimpleBBQRegistry.COOKED_BEEF_SKEWER.get(),
                SimpleBBQRegistry.CHICKEN_SKEWER.get(),
                SimpleBBQRegistry.COOKED_CHICKEN_SKEWER.get(),
                SimpleBBQRegistry.MUTTON_SKEWER.get(),
                SimpleBBQRegistry.COOKED_MUTTON_SKEWER.get(),
                SimpleBBQRegistry.PORK_SKEWER.get(),
                SimpleBBQRegistry.COOKED_PORK_SKEWER.get(),
                SimpleBBQRegistry.RABBIT_SKEWER.get(),
                SimpleBBQRegistry.COOKED_RABBIT_SKEWER.get(),
                SimpleBBQRegistry.COD_SKEWER.get(),
                SimpleBBQRegistry.COOKED_COD_SKEWER.get(),
                SimpleBBQRegistry.SALMON_SKEWER.get(),
                SimpleBBQRegistry.COOKED_SALMON_SKEWER.get(),
                SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(),
                SimpleBBQRegistry.TOAST_SKEWER.get(),
                SimpleBBQRegistry.MUSHROOM_SKEWER.get(),
                SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER.get(),
                SimpleBBQRegistry.POTATO_SKEWER.get(),
                SimpleBBQRegistry.BAKED_POTATO_SKEWER.get()
        );
        var cannotBeSeasonedByHoney = ImmutableSet.of(
                SimpleBBQRegistry.COD_SKEWER.get(),
                SimpleBBQRegistry.COOKED_COD_SKEWER.get(),
                SimpleBBQRegistry.SALMON_SKEWER.get(),
                SimpleBBQRegistry.COOKED_SALMON_SKEWER.get()
        );
        var cannotBeSeasonedByChiliPowder = ImmutableSet.of(
                SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(),
                SimpleBBQRegistry.TOAST_SKEWER.get()
        );
        var cannotBeSeasonedByCumin = ImmutableSet.of(
                SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(),
                SimpleBBQRegistry.TOAST_SKEWER.get()
        );
        var cannotBeSeasonedBySaltAndPepper = ImmutableSet.of(
                SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(),
                SimpleBBQRegistry.TOAST_SKEWER.get()
        );

        this.tag(SimpleBBQItemTags.CAN_BE_SEASONED_BY_HONEY).add(allSkewers.stream().filter(item -> !cannotBeSeasonedByHoney.contains(item)).toArray(Item[]::new));
        this.tag(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CHILI_POWDER).add(allSkewers.stream().filter(item -> !cannotBeSeasonedByChiliPowder.contains(item)).toArray(Item[]::new));
        this.tag(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CUMIN).add(allSkewers.stream().filter(item -> !cannotBeSeasonedByCumin.contains(item)).toArray(Item[]::new));
        this.tag(SimpleBBQItemTags.CAN_BE_SEASONED_BY_SALT_AND_PEPPER).add(allSkewers.stream().filter(item -> !cannotBeSeasonedBySaltAndPepper.contains(item)).toArray(Item[]::new));
    }

    @Override
    public String getName() {
        return "SimpleBBQ Item Tags";
    }
}
