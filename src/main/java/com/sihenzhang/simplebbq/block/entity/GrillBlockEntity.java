package com.sihenzhang.simplebbq.block.entity;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.util.CampfireData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class GrillBlockEntity extends BlockEntity {
    private static final int BURN_COOL_SPEED = 2;
    private static final int SLOT_NUM = 2;

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
    private final CampfireData campfireData = new CampfireData();
    private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> inventory);
    private final int[] cookingProgress = new int[SLOT_NUM];
    private final int[] cookingTime = new int[SLOT_NUM];

    public GrillBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleBBQRegistry.GRILL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        var data = SuperDirtyCrockPotTMAdvancedTempStateDataHolder.get(getBlockPos());
        if (data != null) {
            campfireData.deserialize(data.serialize());
        }
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GrillBlockEntity pBlockEntity) {
        var hasChanged = false;

        if (pState.hasProperty(GrillBlock.HEATED) && pState.getValue(GrillBlock.HEATED)) {
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

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public CampfireData getCampfireData() {
        return campfireData;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        campfireData.deserialize(pTag.getCompound("CampfireData"));
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
        pTag.put("CampfireData", campfireData.serialize());
        pTag.put("Inventory", inventory.serializeNBT());
        pTag.putIntArray("CookingTimes", cookingProgress);
        pTag.putIntArray("CookingTotalTimes", cookingTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("CampfireData", campfireData.serialize());
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return inventoryCap.cast();
        }
        return super.getCapability(cap, side);
    }
}
