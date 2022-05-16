package com.sihenzhang.simplebbq.util;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public final class CampfireData {
    public ResourceLocation registryName = Blocks.AIR.getRegistryName();
    public boolean lit = false;
    public Direction direction = Direction.NORTH;

    public CampfireData() {
    }

    public CampfireData(BlockState state) {
        if (!(state.getBlock() == Blocks.CAMPFIRE || state.getBlock() == Blocks.SOUL_CAMPFIRE)) {
            throw new IllegalArgumentException("State must be a campfire");
        }
        this.registryName = state.getBlock().getRegistryName();
        this.lit = state.getValue(CampfireBlock.LIT);
        this.direction = state.getValue(CampfireBlock.FACING);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("RegistryName", this.registryName.toString());
        tag.putBoolean("Lit", this.lit);
        tag.putString("Direction", this.direction.name());
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        registryName = new ResourceLocation(tag.getString("RegistryName"));
        lit = tag.getBoolean("Lit");
        direction = Direction.valueOf(tag.getString("Direction"));
    }

    public BlockState toBlockState() {
        if (this.registryName == null) {
            return Blocks.AIR.defaultBlockState();
        }
        //noinspection ConstantConditions
        var state = ForgeRegistries.BLOCKS.getValue(registryName).defaultBlockState();
        if (isCampfire(state)) {
            state = state.setValue(CampfireBlock.LIT, lit);
            state = state.setValue(CampfireBlock.FACING, direction);
        }
        return state;
    }

    public static boolean isCampfire(BlockState state) {
        return state.is(BlockTags.CAMPFIRES);
    }
}
