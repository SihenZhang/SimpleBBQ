package com.sihenzhang.simplebbq.block.entity;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkeweringTableBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SKEWERING_RECIPE_TYPE.get(), new SimpleContainer(stack), level).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markUpdated();
        }
    };

    public SkeweringTableBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.put("Inventory", inventory.serializeNBT());
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean canBeSkewered(ItemStack stack) {
        return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SKEWERING_RECIPE_TYPE.get(), new SimpleContainer(stack), level).isPresent();
    }

    public boolean placeFood(Player player, InteractionHand hand) {
        var stackInHand = player.getItemInHand(hand);
        if (stackInHand.isEmpty()) {
            return false;
        }
        var remainStack = inventory.insertItem(0, player.getAbilities().instabuild ? stackInHand.copy() : stackInHand, false);
        if (remainStack.getCount() == stackInHand.getCount()) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, remainStack);
        }
        return true;
    }

    public boolean removeFood(Player player, InteractionHand hand) {
        if (!player.getItemInHand(hand).isEmpty()) {
            return false;
        }
        var stackInInventory = inventory.getStackInSlot(0);
        if (stackInInventory.isEmpty()) {
            return false;
        }
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        player.setItemInHand(hand, stackInInventory);
        return true;
    }

    public boolean skewer(ItemStack skewer, Player player) {
        if (skewer.isEmpty()) {
            return false;
        }
        var container = new RecipeWrapper(inventory);
        var optionalRecipe = level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SKEWERING_RECIPE_TYPE.get(), container, level);
        if (optionalRecipe.isEmpty()) {
            return false;
        }
        var recipe = optionalRecipe.get();
        var result = recipe.assemble(container);
        inventory.extractItem(0, recipe.getCount(), false);
        skewer.shrink(1);
        if (player != null) {
            ItemHandlerHelper.giveItemToPlayer(player, result);
        } else {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), result);
        }
        return true;
    }

    private void markUpdated() {
        this.setChanged();
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
}
