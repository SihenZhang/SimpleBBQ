package com.sihenzhang.simplebbq.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class GrillRenderer implements BlockEntityRenderer<GrillBlockEntity> {
    public GrillRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(GrillBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

    }
}
