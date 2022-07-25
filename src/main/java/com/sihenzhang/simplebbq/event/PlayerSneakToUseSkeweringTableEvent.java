package com.sihenzhang.simplebbq.event;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class PlayerSneakToUseSkeweringTableEvent {
    @SubscribeEvent
    public static void onBlockRightClick(final PlayerInteractEvent.RightClickBlock event) {
        var level = event.getWorld();
        var pos = event.getPos();
        if (level.getBlockState(pos).getBlock() instanceof SkeweringTableBlock) {
            if (level.getBlockEntity(pos) instanceof SkeweringTableBlockEntity skeweringTableBlockEntity) {
                var player = event.getPlayer();
                var stackInHand = event.getItemStack();
                if (player.isShiftKeyDown() && stackInHand.is(SimpleBBQItemTags.SKEWER)) {
                    event.setCanceled(true);
                    if (!level.isClientSide() && skeweringTableBlockEntity.skewer(player.getAbilities().instabuild ? stackInHand.copy() : stackInHand, player)) {
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }
                    event.setCancellationResult(InteractionResult.CONSUME);
                }
            }
        }
    }
}
