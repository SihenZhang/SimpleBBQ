package com.sihenzhang.simplebbq.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;
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
            return getCookingRecipe(new SimpleContainer(stack), level).isPresent();
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
                        var result = pBlockEntity.getCookingRecipe(container, pLevel).map(recipe -> recipe.assemble(container)).orElse(stackInSlot);
                        var seasoningTag = stackInSlot.getTagElement("Seasoning");
                        if (seasoningTag != null) {
                            seasoningTag.putBoolean("HasEffect", true);
                            result.addTagElement("Seasoning", seasoningTag);
                        }
                        Containers.dropItemStack(pLevel, pPos.getX(), (double) pPos.getY() + 1.0D, pPos.getZ(), result);
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

            var facing = pState.getValue(GrillBlock.FACING);
            for (var i = 0; i < pBlockEntity.inventory.getSlots(); i++) {
                if (!pBlockEntity.inventory.getStackInSlot(i).isEmpty() && random.nextFloat() < 0.2F) {
                    var x = (double) pPos.getX() + 0.5D + (facing.getAxis() == Direction.Axis.Z ? (0.4D * i - 0.2D) * facing.getStepZ() : 0.0D);
                    var y = (double) pPos.getY() + 1.1D;
                    var z = (double) pPos.getZ() + 0.5D + (facing.getAxis() == Direction.Axis.X ? (0.4D * i - 0.2D) * facing.getStepX() : 0.0D);

                    for (var j = 0; j < 4; j++) {
                        pLevel.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
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

    public <C extends Container> Optional<? extends AbstractCookingRecipe> getCookingRecipe(C pInventory, Level pLevel) {
        var grillCookingRecipe = pLevel.getRecipeManager().getRecipeFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), pInventory, pLevel);
        return grillCookingRecipe.isPresent() ? grillCookingRecipe : pLevel.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, pInventory, pLevel);
    }

    public Optional<? extends AbstractCookingRecipe> getCookableRecipe(ItemStack input) {
        for (var i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return this.getCookingRecipe(new SimpleContainer(input), level);
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

    public boolean removeFood(Player player, InteractionHand hand, boolean isHittingLeftSide) {
        if (!player.getItemInHand(hand).isEmpty()) {
            return false;
        }
        var stackInInventory = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
        if (stackInInventory.isEmpty()) {
            return false;
        }
        inventory.setStackInSlot(isHittingLeftSide ? 0 : 1, ItemStack.EMPTY);
        player.setItemInHand(hand, stackInInventory);
        return true;
    }

    public Optional<SeasoningRecipe> getSeasoningRecipe(ItemStack seasoning, boolean isHittingLeftSide) {
        var input = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
        return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SEASONING_RECIPE_TYPE.get(), new SimpleContainer(input, seasoning), level);
    }

    public boolean addSeasoning(Player player, ItemStack seasoning, boolean isHittingLeftSide) {
        var input = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
        if (input.isEmpty()) {
            return false;
        }
        var container = new SimpleContainer(input, seasoning);
        var optionalRecipe = level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SEASONING_RECIPE_TYPE.get(), container, level);
        if (optionalRecipe.isEmpty()) {
            return false;
        }
        var recipe = optionalRecipe.get();
        var result = recipe.assemble(container);
        if (result.isEmpty()) {
            return false;
        }
        inventory.setStackInSlot(isHittingLeftSide ? 0 : 1, result);
        var remainingItems = recipe.getRemainingItems(container);
        if (!remainingItems.isEmpty()) {
            remainingItems.forEach(item -> ItemHandlerHelper.giveItemToPlayer(player, item));
        }
        return true;
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
