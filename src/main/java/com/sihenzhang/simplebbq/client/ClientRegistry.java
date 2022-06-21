package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.client.renderer.blockentity.GrillRenderer;
import com.sihenzhang.simplebbq.client.renderer.blockentity.SkeweringTableRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SimpleBBQ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(SimpleBBQRegistry.GRILL_BLOCK.get(), RenderType.cutout()));
    }

    @SubscribeEvent
    public static void onRegisterRenderer(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SimpleBBQRegistry.GRILL_BLOCK_ENTITY.get(), GrillRenderer::new);
        event.registerBlockEntityRenderer(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ENTITY.get(), SkeweringTableRenderer::new);
    }
}
