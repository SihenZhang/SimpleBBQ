package com.sihenzhang.simplebbq.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class GrillBlockEntity extends BlockEntity {
    private static final int BURN_COOL_SPEED = 2;
    private static final int SLOT_NUM = 2;

    private final CampfireData campfireData = new CampfireData();
    private final ItemStackHandler inventory = new ItemStackHandler(SLOT_NUM) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), new SimpleContainer(stack), level).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markUpdated();
        }
    };
    private final int[] cookingProgress = new int[SLOT_NUM];
    private final int[] cookingTime = new int[SLOT_NUM];

    public GrillBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleBBQRegistry.GRILL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void initCampfireState(CampfireData data) {
        if (level == null || data == null) {
            return;
        }
        campfireData.deserializeNBT(data.serializeNBT());
        var state = this.getBlockState().setValue(GrillBlock.LIT, campfireData.lit);
        level.setBlockAndUpdate(worldPosition, state);
        setChanged(level, worldPosition, state);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GrillBlockEntity pBlockEntity) {
        var hasChanged = false;

        // sync the campfire state
        if (pState.hasProperty(GrillBlock.WATERLOGGED) && pState.getValue(GrillBlock.WATERLOGGED) && pBlockEntity.campfireData.lit) {
            pBlockEntity.campfireData.lit = false;
            hasChanged = true;
        }
        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT) != pBlockEntity.campfireData.lit) {
            pState = pState.setValue(GrillBlock.LIT, pBlockEntity.campfireData.lit);
            pLevel.setBlockAndUpdate(pPos, pState);
            hasChanged = true;
        }

        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT)) {
            for (var i = 0; i < pBlockEntity.inventory.getSlots(); i++) {
                var stackInSlot = pBlockEntity.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    hasChanged = true;
                    pBlockEntity.cookingProgress[i]++;
                    if (pBlockEntity.cookingProgress[i] >= pBlockEntity.cookingTime[i]) {
                        var container = new SimpleContainer(stackInSlot);
                        var result = pLevel.getRecipeManager().getRecipeFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), container, pLevel).map(recipe -> recipe.assemble(container)).orElse(stackInSlot);
                        Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), result);
                        pBlockEntity.inventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        } else {
            for (var i = 0; i < pBlockEntity.inventory.getSlots(); i++) {
                var stackInSlot = pBlockEntity.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    hasChanged = true;
                    pBlockEntity.cookingProgress[i] = Mth.clamp(pBlockEntity.cookingProgress[i] - BURN_COOL_SPEED, 0, pBlockEntity.cookingTime[i]);
                }
            }
        }

        if (hasChanged) {
            setChanged(pLevel, pPos, pState);
        }
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, GrillBlockEntity pBlockEntity) {
        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT) && pBlockEntity.campfireData.lit) {
            var random = pLevel.getRandom();
            if (random.nextFloat() < 0.05F) {
                GrillBlock.makeCampfireParticles(pLevel, pPos, false);
            }
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public CampfireData getCampfireData() {
        return campfireData;
    }

    public void setCampfireData(CampfireData data) {
        if (level != null) {
            campfireData.deserializeNBT(data.serializeNBT());
            this.markUpdated();
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        campfireData.deserializeNBT(pTag.getCompound("CampfireData"));
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
        if (pTag.contains("CookingTimes", Tag.TAG_INT_ARRAY)) {
            var cookingProcessArray = pTag.getIntArray("CookingTimes");
            System.arraycopy(cookingProcessArray, 0, cookingProgress, 0, Math.min(cookingProgress.length, cookingProcessArray.length));
        }
        if (pTag.contains("CookingTotalTimes", Tag.TAG_INT_ARRAY)) {
            var cookingTimeArray = pTag.getIntArray("CookingTotalTimes");
            System.arraycopy(cookingTimeArray, 0, cookingTime, 0, Math.min(cookingTime.length, cookingTimeArray.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("CampfireData", campfireData.serializeNBT());
        pTag.put("Inventory", inventory.serializeNBT());
        pTag.putIntArray("CookingTimes", cookingProgress);
        pTag.putIntArray("CookingTotalTimes", cookingTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.put("CampfireData", campfireData.serializeNBT());
        tag.put("Inventory", inventory.serializeNBT());
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public Optional<GrillCookingRecipe> getCookableRecipe(ItemStack input) {
        for (var i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), new SimpleContainer(input), level);
            }
        }
        return Optional.empty();
    }

    public boolean placeFood(ItemStack input, int cookTime) {
        for (var i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                cookingTime[i] = cookTime;
                cookingProgress[i] = 0;
                inventory.setStackInSlot(i, input.split(1));
                return true;
            }
        }
        return false;
    }

    private void markUpdated() {
        this.setChanged();
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public static final class CampfireData implements INBTSerializable<CompoundTag> {
        public ResourceLocation registryName = Blocks.AIR.getRegistryName();
        public boolean lit = false;
        public Direction facing;

        public CampfireData() {
        }

        public CampfireData(BlockState state) {
            Preconditions.checkArgument(GrillBlock.isCampfire(state), "State must be a Campfire.");
            this.registryName = state.getBlock().getRegistryName();
            this.lit = state.getValue(BlockStateProperties.LIT);
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                this.facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("RegistryName", registryName.toString());
            tag.putBoolean("Lit", lit);
            if (facing != null) {
                tag.putString("Facing", facing.name());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            registryName = new ResourceLocation(nbt.getString("RegistryName"));
            lit = nbt.getBoolean("Lit");
            if (nbt.contains("Facing", Tag.TAG_STRING)) {
                facing = Direction.valueOf(nbt.getString("Facing"));
            }
        }

        public BlockState toBlockState() {
            if (registryName == null) {
                return Blocks.AIR.defaultBlockState();
            }
            var state = ForgeRegistries.BLOCKS.getValue(registryName).defaultBlockState();
            if (state.hasProperty(BlockStateProperties.LIT)) {
                state = state.setValue(BlockStateProperties.LIT, lit);
            }
            if (facing != null && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
            }
            return state;
        }

        public CampfireData copy() {
            var newCampfireData = new GrillBlockEntity.CampfireData();
            newCampfireData.deserializeNBT(this.serializeNBT());
            return newCampfireData;
        }
    }
}
