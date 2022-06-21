package com.sihenzhang.simplebbq.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.client.model.data.EmptyModelData;

public class GrillRenderer implements BlockEntityRenderer<GrillBlockEntity> {
    public GrillRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(GrillBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        var mc = Minecraft.getInstance();

        var direction = pBlockEntity.getBlockState().getValue(GrillBlock.FACING);
        var inventory = pBlockEntity.getInventory();
        var posInt = (int) pBlockEntity.getBlockPos().asLong();
        for (var j = 0; j < inventory.getSlots(); j++) {
            var itemStack = inventory.getStackInSlot(j);
            if (!itemStack.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5D, 0.98285D, 0.5D);
                float f = -direction.toYRot();
                pPoseStack.mulPose(Vector3f.YP.rotationDegrees(f));
                pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                pPoseStack.translate(0.2D - 0.4D * j, 0.0D, 0.0D);
                pPoseStack.scale(0.45F, 0.45F, 0.45F);
                mc.getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, posInt + j);
                pPoseStack.popPose();
            }
        }

        var blockState = pBlockEntity.getCampfireData().toBlockState();
        pPoseStack.pushPose();
        mc.getBlockRenderer().renderSingleBlock(blockState, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();
    }
}
