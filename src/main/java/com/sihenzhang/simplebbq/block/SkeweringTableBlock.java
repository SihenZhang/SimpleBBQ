package com.sihenzhang.simplebbq.block;

import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class SkeweringTableBlock extends BaseEntityBlock {
    public SkeweringTableBlock() {
        super(Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SkeweringTableBlockEntity skeweringTableBlockEntity) {
            var stackInHand = pPlayer.getItemInHand(pHand);
            if (stackInHand.is(SimpleBBQItemTags.SKEWER)) {
                if (!pLevel.isClientSide && skeweringTableBlockEntity.skewer(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, pPlayer)) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
            if (skeweringTableBlockEntity.canBeSkewered(stackInHand)) {
                if (!pLevel.isClientSide() && skeweringTableBlockEntity.placeFood(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, pPlayer, pHand)) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            var blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SkeweringTableBlockEntity skeweringTableBlockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(skeweringTableBlockEntity.getInventory()));
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SkeweringTableBlockEntity(pPos, pState);
    }
}
