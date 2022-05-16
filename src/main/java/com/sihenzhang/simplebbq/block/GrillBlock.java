package com.sihenzhang.simplebbq.block;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class GrillBlock extends BaseEntityBlock {
    public static final BooleanProperty HEATED = BooleanProperty.create("heated");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GrillBlock() {
        super(Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> state.getValue(HEATED) ? 15 : 0).noCollission());
        this.registerDefaultState(stateDefinition.any().setValue(HEATED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
            var stackInHand = pPlayer.getItemInHand(pHand);
            var optionalRecipe = grillBlockEntity.getCookableRecipe(stackInHand);
            if (optionalRecipe.isPresent()) {
                if (!pLevel.isClientSide && grillBlockEntity.placeFood(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, optionalRecipe.get().getCookingTime())) {
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
            if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
                Containers.dropContents(pLevel, pPos, new RecipeWrapper(grillBlockEntity.getInventory()));
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();
        return this.defaultBlockState().setValue(HEATED, this.isLitCampfire(level.getBlockState(pos.below()))).setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return pDirection == Direction.DOWN ? pState.setValue(HEATED, this.isLitCampfire(pNeighborState)) : pState;
    }

    private boolean isLitCampfire(BlockState pState) {
        return pState.is(BlockTags.CAMPFIRES) && pState.hasProperty(BlockStateProperties.LIT) && pState.getValue(BlockStateProperties.LIT);
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
        pBuilder.add(HEATED, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrillBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, SimpleBBQRegistry.GRILL_BLOCK_ENTITY.get(), GrillBlockEntity::serverTick);
    }
}
