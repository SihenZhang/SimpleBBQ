package com.sihenzhang.simplebbq.item;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.entity.SuperDirtyCrockPotTMAdvancedTempStateDataHolder;
import com.sihenzhang.simplebbq.util.CampfireData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class GrillItem extends BlockItem {
    public GrillItem() {
        super(SimpleBBQRegistry.GRILL_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        BlockState state = level.getBlockState(pContext.getClickedPos());
        if (CampfireData.isCampfire(state)) {
            SuperDirtyCrockPotTMAdvancedTempStateDataHolder.put(
                    pContext.getClickedPos(),
                    new CampfireData(state)
            );
            level.setBlock(pContext.getClickedPos(), Blocks.AIR.defaultBlockState(), 3);
        }
        return super.useOn(pContext);
    }
}
