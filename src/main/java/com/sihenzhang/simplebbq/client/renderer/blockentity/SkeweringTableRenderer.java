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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

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
            var itemRenderer = Minecraft.getInstance().getItemRenderer();
            var count = getRenderCount(itemStack);
            var isBlockItem = itemRenderer.getModel(itemStack, pBlockEntity.getLevel(), null, posInt).isGui3d();
            var random = new Random(posInt);
            pPoseStack.pushPose();
            // center the item/block on the table
            if (isBlockItem) {
                pPoseStack.translate(0.5D, 1.19D, 0.5D);
            } else {
                pPoseStack.translate(0.5D, 1.02D, 0.5D);
            }
            // rotate the item/block to face the table
            pPoseStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot()));
            // rotate the item to lay down on the table
            if (!isBlockItem) {
                pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            }
            // resize the item/block on the table
            if (isBlockItem) {
                pPoseStack.scale(0.75F, 0.75F, 0.75F);
            } else {
                pPoseStack.scale(0.6F, 0.6F, 0.6F);
            }
            for (var i = 0; i < count; i++) {
                pPoseStack.pushPose();
                // stack the items/blocks on the table
                if (isBlockItem) {
                    pPoseStack.translate(i > 0 ? Mth.nextDouble(random, -0.0625D, 0.0625D) : 0.0D, 0.0625D * i, i > 0 ? Mth.nextDouble(random, -0.0625D, 0.0625D) : 0.0D);
                } else {
                    pPoseStack.translate(i > 0 ? Mth.nextDouble(random, -0.075D, 0.075D) : 0.0D, i > 0 ? Mth.nextDouble(random, -0.075D, 0.075D) : 0.0D, -0.06D * i);
                    pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(i > 0 ? Mth.nextFloat(random, -15.0F, 15.0F) : 0.0F));
                }
                // render the item/block on the table
                itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, posInt);
                pPoseStack.popPose();
            }
            pPoseStack.popPose();
        }
    }

    private static int getRenderCount(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        if (stack.getCount() > 48) {
            return 5;
        }
        if (stack.getCount() > 32) {
            return 4;
        }
        if (stack.getCount() > 16) {
            return 3;
        }
        if (stack.getCount() > 1) {
            return 2;
        }
        return 1;
    }
}
