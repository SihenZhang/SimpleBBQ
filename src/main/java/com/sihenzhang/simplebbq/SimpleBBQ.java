package com.sihenzhang.simplebbq;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleBBQ.MOD_ID)
public class SimpleBBQ {
    public static final String MOD_ID = "simplebbq";

    public static final CreativeModeTab TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance();
        }
    };

    public SimpleBBQ() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SimpleBBQConfig.COMMON_CONFIG);

        SimpleBBQRegistry.ITEMS.register(modEventBus);
        SimpleBBQRegistry.BLOCKS.register(modEventBus);
        SimpleBBQRegistry.BLOCK_ENTITIES.register(modEventBus);
        SimpleBBQRegistry.PARTICLE_TYPES.register(modEventBus);
        SimpleBBQRegistry.POI_TYPES.register(modEventBus);
        SimpleBBQRegistry.PROFESSIONS.register(modEventBus);
        SimpleBBQRegistry.RECIPE_TYPES.register(modEventBus);
        SimpleBBQRegistry.RECIPE_SERIALIZERS.register(modEventBus);
    }
}
