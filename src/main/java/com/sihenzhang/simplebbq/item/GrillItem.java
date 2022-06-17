package com.sihenzhang.simplebbq.item;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.entity.SuperDirtyCrockPotTMAdvancedTempStateDataHolder;
import com.sihenzhang.simplebbq.util.CampfireData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;

public class GrillItem extends BlockItem {
    public GrillItem() {
        super(SimpleBBQRegistry.GRILL_BLOCK.get(), new Item.Properties().tab(SimpleBBQ.TAB));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();
        var state = level.getBlockState(pos);
        if (CampfireData.isCampfire(state)) {
            var player = pContext.getPlayer();
            SuperDirtyCrockPotTMAdvancedTempStateDataHolder.put(pos, new CampfireData(state));
            level.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1.0F, 1.5F);
            level.setBlockAndUpdate(pos, SimpleBBQRegistry.GRILL_BLOCK.get().defaultBlockState());
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            player.awardStat(Stats.ITEM_USED.get(pContext.getItemInHand().getItem()));
            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }
}
