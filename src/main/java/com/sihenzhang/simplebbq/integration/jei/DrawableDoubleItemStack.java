package com.sihenzhang.simplebbq.integration.jei;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DrawableDoubleItemStack implements IDrawable {
    private final Supplier<ItemStack> primarySupplier;
    private final Supplier<ItemStack> secondarySupplier;

    public DrawableDoubleItemStack(ItemStack primary, ItemStack secondary) {
        this.primarySupplier = Suppliers.memoize(() -> primary);
        this.secondarySupplier = Suppliers.memoize(() -> secondary);
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void draw(PoseStack poseStack, int xOffset, int yOffset) {
        var primaryStack = primarySupplier.get();
        var secondaryStack = secondarySupplier.get();
        RenderSystem.enableDepthTest();
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 0);
        if (primaryStack != null && !primaryStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(1, 1, 0);
            renderItemStack(poseStack, primaryStack);
            poseStack.popPose();
        }
        if (secondaryStack != null && !secondaryStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(8, 8, 100);
            poseStack.scale(0.6F, 0.6F, 0.6F);
            renderItemStack(poseStack, secondaryStack);
            poseStack.popPose();
        }
        poseStack.popPose();
        RenderSystem.disableDepthTest();
    }

    private static void renderItemStack(PoseStack poseStack, ItemStack stack) {
        var modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        var mc = Minecraft.getInstance();
        var itemRenderer = mc.getItemRenderer();
        itemRenderer.renderAndDecorateFakeItem(stack, 0, 0);
        itemRenderer.renderGuiItemDecorations(mc.font, stack, 0, 0);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
