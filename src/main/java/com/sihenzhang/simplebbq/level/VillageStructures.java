/********************************************************************************
 * MIT License
 *
 * Copyright (c) 2020 vectorwing
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ********************************************************************************/
package com.sihenzhang.simplebbq.level;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class VillageStructures {
    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        var templatePools = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).get();
        var processorLists = event.getServer().registryAccess().registry(Registry.PROCESSOR_LIST_REGISTRY).get();

//        VillageStructures.addBuildingToPool(templatePools, processorLists, RLUtils.createVanillaRL("village/plains/houses"), RLUtils.createRL("village/houses/plains_bbq_camp").toString(), 5);
//        VillageStructures.addBuildingToPool(templatePools, processorLists, RLUtils.createVanillaRL("village/snowy/houses"), RLUtils.createRL("village/houses/snowy_bbq_camp").toString(), 3);
//        VillageStructures.addBuildingToPool(templatePools, processorLists, RLUtils.createVanillaRL("village/savanna/houses"), RLUtils.createRL("village/houses/savanna_bbq_camp").toString(), 4);
//        VillageStructures.addBuildingToPool(templatePools, processorLists, RLUtils.createVanillaRL("village/desert/houses"), RLUtils.createRL("village/houses/desert_bbq_camp").toString(), 3);
        VillageStructures.addBuildingToPool(templatePools, processorLists, RLUtils.createVanillaRL("village/taiga/houses"), RLUtils.createRL("village/houses/taiga_bbq_camp").toString(), 2);
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        var pool = templatePoolRegistry.get(poolRL);
        if (pool == null) {
            return;
        }

        var emptyProcessor = RLUtils.createVanillaRL("empty");
        var processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registry.PROCESSOR_LIST_REGISTRY, emptyProcessor));

        var piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

        pool.rawTemplates = Util.make(new ArrayList<>(pool.rawTemplates), newRawTemplates -> newRawTemplates.add(Pair.of(piece, weight)));

        for (var i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }
    }
}
