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
        for (var i = 0; i < inventory.getSlots(); i++) {
            var itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                var itemRenderer = mc.getItemRenderer();
                var isBlockItem = itemRenderer.getModel(itemStack, pBlockEntity.getLevel(), null, 0).isGui3d();
                pPoseStack.pushPose();
                // center the item/block on the grill
                if (isBlockItem) {
                    pPoseStack.translate(0.5D, 1.11885D, 0.5D);
                } else {
                    pPoseStack.translate(0.5D, 0.98285D, 0.5D);
                }
                // rotate the item/block to face the grill
                pPoseStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot()));
                // rotate the item to lay down on the grill
                if (!isBlockItem) {
                    pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                }
                // move the item/block to the specified position
                pPoseStack.translate(0.2D - 0.4D * i, 0.0D, 0.0D);
                // resize the item/block
                if (isBlockItem) {
                    pPoseStack.scale(0.6F, 0.6F, 0.6F);
                } else {
                    pPoseStack.scale(0.45F, 0.45F, 0.45F);
                }
                // render the item/block on the grill
                itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, posInt + i);
                pPoseStack.popPose();
            }
        }

        // render campfire block
        var blockState = pBlockEntity.getCampfireData().toBlockState();
        pPoseStack.pushPose();
        mc.getBlockRenderer().renderSingleBlock(blockState, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();
    }
}
