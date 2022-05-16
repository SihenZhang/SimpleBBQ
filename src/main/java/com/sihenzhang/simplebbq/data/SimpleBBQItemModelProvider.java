package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SimpleBBQItemModelProvider extends ItemModelProvider {
    public SimpleBBQItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("grill", RLUtils.createRL("block/grill"));
    }
}
