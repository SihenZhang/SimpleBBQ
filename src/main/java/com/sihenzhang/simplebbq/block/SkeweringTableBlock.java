package com.sihenzhang.simplebbq.block;

import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class SkeweringTableBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SkeweringTableBlock() {
        super(Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion());
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SkeweringTableBlockEntity skeweringTableBlockEntity) {
            var stackInHand = pPlayer.getItemInHand(pHand);

            // try to remove
            if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty()) {
                if (!pLevel.isClientSide() && skeweringTableBlockEntity.removeFood(pPlayer, pHand)) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }

            // try to skewer
            if (stackInHand.is(SimpleBBQItemTags.SKEWER)) {
                if (!pLevel.isClientSide() && skeweringTableBlockEntity.skewer(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, pPlayer)) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }

            // try to place
            if (skeweringTableBlockEntity.canBeSkewered(stackInHand)) {
                if (!pLevel.isClientSide() && skeweringTableBlockEntity.placeFood(pPlayer, pHand)) {
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SkeweringTableBlockEntity(pPos, pState);
    }
}
