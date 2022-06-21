package com.sihenzhang.simplebbq.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SkeweringTableRenderer implements BlockEntityRenderer<SkeweringTableBlockEntity> {
    public SkeweringTableRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(SkeweringTableBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        var direction = pBlockEntity.getBlockState().getValue(SkeweringTableBlock.FACING);
        var inventory = pBlockEntity.getInventory();
        var posInt = (int) pBlockEntity.getBlockPos().asLong();
        var itemStack = inventory.getStackInSlot(0);
        if (!itemStack.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 1.05D, 0.5D);
            float f = -direction.toYRot();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(f));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            pPoseStack.scale(0.6F, 0.6F, 0.6F);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, posInt);
            pPoseStack.popPose();
        }
    }
}
