package com.sihenzhang.simplebbq.event;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class AddingTradesEvent {
    @SubscribeEvent
    public static void onVillagerTrades(final VillagerTradesEvent event) {
        var profession = event.getType();
        if (SimpleBBQRegistry.SKEWERMAN.getId().equals(profession.getRegistryName())) {
            var trades = event.getTrades();

            var noviceTrades = trades.get(1);
            noviceTrades.add(itemsAndEmeraldsToItems(Items.BROWN_MUSHROOM, 5, 1, SimpleBBQRegistry.MUSHROOM_SKEWER.get(), 5, 16, 2, 0.05F));
            noviceTrades.add(itemsAndEmeraldsToItems(Items.POTATO, 5, 1, SimpleBBQRegistry.POTATO_SKEWER.get(), 5, 16, 2, 0.05F));
            noviceTrades.add(itemsAndEmeraldsToItems(Items.COD, 4, 1, SimpleBBQRegistry.COD_SKEWER.get(), 4, 16, 2, 0.05F));
            noviceTrades.add(itemsAndEmeraldsToItems(Items.CHICKEN, 4, 1, SimpleBBQRegistry.CHICKEN_SKEWER.get(), 4, 16, 2, 0.05F));
            noviceTrades.add(itemsAndEmeraldsToItems(Items.RABBIT, 3, 1, SimpleBBQRegistry.RABBIT_SKEWER.get(), 3, 16, 2, 0.05F));
            noviceTrades.add(itemsAndEmeraldsToItems(Items.BREAD, 3, 1, SimpleBBQRegistry.BREAD_SLICE_SKEWER.get(), 3, 16, 2, 0.05F));

            var apprenticeTrades = trades.get(2);
            apprenticeTrades.add(itemsForEmeralds(Items.BOWL, 20, 1, 16, 10, 0.05F));
            apprenticeTrades.add(itemsForEmeralds(Items.SUGAR, 26, 1, 16, 10, 0.05F));
            apprenticeTrades.add(emeraldsForItems(4, SimpleBBQRegistry.GRILL_BLOCK.get(), 1, 12, 5, 0.05F));
            apprenticeTrades.add(emeraldsForItems(4, Items.CAMPFIRE, 1, 12, 5, 0.05F));

            var journeymanTrades = trades.get(3);
            journeymanTrades.add(itemsAndEmeraldsToItems(Items.RED_MUSHROOM, 1, 2, SimpleBBQRegistry.CHILI_POWDER.get(), 6, 12, 10, 0.2F));
            journeymanTrades.add(itemsAndEmeraldsToItems(Items.WHEAT_SEEDS, 1, 2, SimpleBBQRegistry.CUMIN.get(), 6, 12, 10, 0.2F));
            journeymanTrades.add(itemsAndEmeraldsToItems(Items.BEETROOT_SEEDS, 1, 2, SimpleBBQRegistry.SALT_AND_PEPPER.get(), 6, 12, 10, 0.2F));

            var expertTrades = trades.get(4);
            expertTrades.add(itemsAndEmeraldsToItems(Items.PORKCHOP, 4, 1, SimpleBBQRegistry.PORK_SKEWER.get(), 4, 12, 15, 0.05F));
            expertTrades.add(itemsAndEmeraldsToItems(Items.MUTTON, 4, 1, SimpleBBQRegistry.MUTTON_SKEWER.get(), 4, 12, 15, 0.05F));
            expertTrades.add(itemsAndEmeraldsToItems(Items.BEEF, 3, 1, SimpleBBQRegistry.BEEF_SKEWER.get(), 3, 12, 15, 0.05F));
            expertTrades.add(itemsAndEmeraldsToItems(Items.SALMON, 3, 1, SimpleBBQRegistry.SALMON_SKEWER.get(), 3, 12, 15, 0.05F));

            var masterTrades = trades.get(5);
            masterTrades.add(itemsForEmeralds(Items.BAMBOO, 18, 1, 12, 30, 0.05F));
        }
    }

    private static VillagerTrades.ItemListing emeraldsForItems(int emeraldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(emeraldCost, new ItemStack(toItem, toCount), maxUses, villagerXp, priceMultiplier);
    }

    private static VillagerTrades.ItemListing itemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(fromItem, fromCount), new ItemStack(toItem, toCount), maxUses, villagerXp, priceMultiplier);
    }

    private static VillagerTrades.ItemListing itemsForEmeralds(ItemLike fromItem, int fromCount, int emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(new ItemStack(fromItem, fromCount), new ItemStack(Items.EMERALD, emeraldCount), maxUses, villagerXp, priceMultiplier);
    }
}
