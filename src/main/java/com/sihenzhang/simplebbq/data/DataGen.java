package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            var blockTagsProvider = new SimpleBBQBlockTagsProvider(generator, SimpleBBQ.MOD_ID, helper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new SimpleBBQItemTagsProvider(generator, blockTagsProvider, SimpleBBQ.MOD_ID, helper));
            generator.addProvider(new SimpleBBQRecipeProvider(generator));
        }
    }
}
