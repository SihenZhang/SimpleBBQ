package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            var blockTagsProvider = new SimpleBBQBlockTagsProvider(generator, helper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new SimpleBBQItemTagsProvider(generator, blockTagsProvider, helper));
            generator.addProvider(new SimpleBBQRecipeProvider(generator));
        }
        if (event.includeClient()) {
            var blockStateProvider = new SimpleBBQBlockStateProvider(generator, helper);
            generator.addProvider(blockStateProvider);
            generator.addProvider(new SimpleBBQItemModelProvider(generator, blockStateProvider.models().existingFileHelper));
        }
    }
}
