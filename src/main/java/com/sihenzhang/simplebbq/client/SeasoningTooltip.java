package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SimpleBBQ.MOD_ID)
public class SeasoningTooltip {
    @SubscribeEvent
    public static void onTooltip(final ItemTooltipEvent event) {
        var itemStack = event.getItemStack();
        var seasoningTag = itemStack.getTagElement("Seasoning");
        if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            for (var i = 0; i < seasoningList.size(); i++) {
                event.getToolTip().add(new TextComponent(seasoningList.getString(i)));
            }
        }
    }
}
