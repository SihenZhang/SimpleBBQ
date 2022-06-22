package com.sihenzhang.simplebbq.block;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class GrillBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    protected static final VoxelShape OUTLINE_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 1.0D, 10.0D, 1.0D),
            Block.box(0.0D, 0.0D, 15.0D, 1.0D, 10.0D, 16.0D),
            Block.box(15.0D, 0.0D, 15.0D, 16.0D, 10.0D, 16.0D),
            Block.box(15.0D, 0.0D, 0.0D, 16.0D, 10.0D, 1.0D),
            Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    );
    protected static final VoxelShape COLLISION_SHAPE = Shapes.join(
            OUTLINE_SHAPE,
            Block.box(1.0D, 15.5D, 1.0D, 15.0D, 16.0D, 15.0D),
            BooleanOp.ONLY_FIRST
    );
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GrillBlock() {
        super(Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> state.getValue(LIT) ? 15 : 0).noOcclusion());
        this.registerDefaultState(stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
            var stackInHand = pPlayer.getItemInHand(pHand);
            var campfireData = grillBlockEntity.getCampfireData();

            // try to light the grill
            if (pState.hasProperty(LIT) && !pState.getValue(LIT) && pState.hasProperty(WATERLOGGED) && !pState.getValue(WATERLOGGED) && isCampfire(campfireData.toBlockState()) && !campfireData.lit) {
                if (stackInHand.getItem() instanceof FlintAndSteelItem) {
                    pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
                    var newCampfireData = campfireData.copy();
                    newCampfireData.lit = true;
                    grillBlockEntity.setCampfireData(newCampfireData);
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_PLACE, pPos);
                    stackInHand.hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pHand));
                    return InteractionResult.sidedSuccess(pLevel.isClientSide());
                }
                if (stackInHand.getItem() instanceof FireChargeItem) {
                    var random = pLevel.getRandom();
                    pLevel.playSound(null, pPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                    var newCampfireData = campfireData.copy();
                    newCampfireData.lit = true;
                    grillBlockEntity.setCampfireData(newCampfireData);
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_PLACE, pPos);
                    if (!pPlayer.getAbilities().instabuild) {
                        stackInHand.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            }

            // try to dowse the grill
            if (pState.hasProperty(LIT) && pState.getValue(LIT) && isCampfire(campfireData.toBlockState()) && campfireData.lit) {
                if (stackInHand.getItem() instanceof ShovelItem) {
                    if (!pLevel.isClientSide()) {
                        pLevel.levelEvent(null, 1009, pPos, 0);
                    }
                    dowse(pPlayer, pLevel, pPos, pState);
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, false));
                    stackInHand.hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pHand));
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            }

            // try to cook
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
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.fireImmune() && pState.getValue(LIT) && pEntity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            var damage = 1.0F;
            var blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
                var block = grillBlockEntity.getCampfireData().toBlockState().getBlock();
                if (block instanceof CampfireBlock campfireBlock) {
                    var campfireDamage = (Integer) ObfuscationReflectionHelper.getPrivateValue(CampfireBlock.class, campfireBlock, "fireDamage");
                    if (campfireDamage != null) {
                        damage = campfireDamage.floatValue();
                    }
                }
            }
            pEntity.hurt(DamageSource.HOT_FLOOR, damage);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
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
        return this.defaultBlockState().setValue(LIT, false).setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER).setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getShapeWithCampfire(COLLISION_SHAPE, pState, pLevel, pPos, pContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getShapeWithCampfire(OUTLINE_SHAPE, pState, pLevel, pPos, pContext);
    }

    @SuppressWarnings("deprecation")
    private VoxelShape getShapeWithCampfire(VoxelShape pBaseShape, BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                return Shapes.or(pBaseShape, campfireState.getBlock().getShape(pState, pLevel, pPos, pContext));
            }
        }
        return pBaseShape;
    }

    public static boolean isCampfire(BlockState pState) {
        return pState.is(BlockTags.CAMPFIRES) && pState.hasProperty(BlockStateProperties.LIT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static void dowse(@Nullable Entity pEntity, LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) {
            for (int i = 0; i < 20; i++) {
                CampfireBlock.makeParticles((Level) pLevel, pPos, false, true);
            }
        }
        var blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
            var newCampfireData = grillBlockEntity.getCampfireData().copy();
            newCampfireData.lit = false;
            grillBlockEntity.setCampfireData(newCampfireData);
        }
        pLevel.gameEvent(pEntity, GameEvent.BLOCK_CHANGE, pPos);
    }

    @Override
    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!pState.getValue(WATERLOGGED) && pFluidState.getType() == Fluids.WATER) {
            if (pState.getValue(LIT)) {
                if (!pLevel.isClientSide()) {
                    pLevel.playSound(null, pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                dowse(null, pLevel, pPos, pState);
            }
            pLevel.setBlock(pPos, pState.setValue(WATERLOGGED, true).setValue(LIT, false), Block.UPDATE_ALL);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        var pos = pHit.getBlockPos();
        var blockEntity = pLevel.getBlockEntity(pos);
        if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
            var campfireData = grillBlockEntity.getCampfireData();
            if (!pLevel.isClientSide && pProjectile.isOnFire() && pProjectile.mayInteract(pLevel, pos) && !pState.getValue(WATERLOGGED) && isCampfire(campfireData.toBlockState()) && !campfireData.lit) {
                var newCampfireData = campfireData.copy();
                newCampfireData.lit = true;
                grillBlockEntity.setCampfireData(newCampfireData);
                pLevel.setBlockAndUpdate(pos, pState.setValue(LIT, true));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
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
        pBuilder.add(LIT, WATERLOGGED, FACING);
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

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}
