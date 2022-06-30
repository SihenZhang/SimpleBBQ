package com.sihenzhang.simplebbq.level;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class VillageStructures {
    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        var templatePools = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).orElseThrow();

        var plainsPoolRL = RLUtils.createVanillaRL("village/plains/houses");
        var savannaPoolRL = RLUtils.createVanillaRL("village/savanna/houses");
        var desertPoolRL = RLUtils.createVanillaRL("village/desert/houses");
        var taigaPoolRL = RLUtils.createVanillaRL("village/taiga/houses");
        var snowyPoolRL = RLUtils.createVanillaRL("village/snowy/houses");

        addBuildingToPool(templatePools, plainsPoolRL, RLUtils.createRL("village/houses/plains_bbq_camp_1"), 1);
        addBuildingToPool(templatePools, plainsPoolRL, RLUtils.createRL("village/houses/plains_bbq_camp_2"), 1);
        addBuildingToPool(templatePools, savannaPoolRL, RLUtils.createRL("village/houses/savanna_bbq_camp_1"), 2);
        addBuildingToPool(templatePools, desertPoolRL, RLUtils.createRL("village/houses/desert_bbq_camp_1"), 2);
        addBuildingToPool(templatePools, taigaPoolRL, RLUtils.createRL("village/houses/taiga_bbq_camp_1"), 2);
        addBuildingToPool(templatePools, snowyPoolRL, RLUtils.createRL("village/houses/snowy_bbq_camp_1"), 3);
        addBuildingToPool(templatePools, snowyPoolRL, RLUtils.createRL("village/houses/snowy_bbq_camp_2"), 1);
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, ResourceLocation poolRL, ResourceLocation nbtPieceRL, int weight) {
        var pool = templatePoolRegistry.get(poolRL);
        if (pool == null) {
            return;
        }
        var piece = SinglePoolElement.single(nbtPieceRL.toString()).apply(StructureTemplatePool.Projection.RIGID);
        pool.rawTemplates = Util.make(new ArrayList<>(pool.rawTemplates), newRawTemplates -> newRawTemplates.add(Pair.of(piece, weight)));
        for (var i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }
    }
}
