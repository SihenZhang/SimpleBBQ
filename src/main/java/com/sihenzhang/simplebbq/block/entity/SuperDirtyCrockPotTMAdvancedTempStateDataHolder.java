package com.sihenzhang.simplebbq.block.entity;

import com.sihenzhang.simplebbq.util.CampfireData;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class SuperDirtyCrockPotTMAdvancedTempStateDataHolder {
    private static final Map<BlockPos, CampfireData> daMap = new HashMap<>();

    static CampfireData get(BlockPos pos) {
        synchronized (daMap) {
            return daMap.remove(pos);
        }
    }

    public static void put(BlockPos pos, CampfireData data) {
        synchronized (daMap) {
            daMap.put(pos, data);
        }
    }
}
