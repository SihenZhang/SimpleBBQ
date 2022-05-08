package com.sihenzhang.simplebbq.block.entity;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkeweringTableBlockEntity extends BlockEntity {
    public SkeweringTableBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }
}
