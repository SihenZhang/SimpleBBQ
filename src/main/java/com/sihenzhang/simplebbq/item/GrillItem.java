package com.sihenzhang.simplebbq.item;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public class GrillItem extends BlockItem {
    public GrillItem() {
        super(SimpleBBQRegistry.GRILL_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();
        var state = level.getBlockState(pos);
        if (GrillBlock.isCampfire(state)) {
            if (!level.isClientSide) {
                GrillBlockEntity.CampfireDataCache.put(level, pos, new GrillBlockEntity.CampfireData(state));
            }
            // remove the old block to replace it with the grill
            level.removeBlock(pos, false);
            // place the grill
            var placeResult = this.place(new BlockPlaceContext(pContext));
            if (placeResult.consumesAction()) {
                return placeResult;
            } else {
                // if failed to place the grill, put the old block back
                level.setBlockAndUpdate(pos, state);
            }
        }
        return super.useOn(pContext);
    }
}
