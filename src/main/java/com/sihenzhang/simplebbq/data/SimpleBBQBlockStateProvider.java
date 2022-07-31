package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SimpleBBQBlockStateProvider extends BlockStateProvider {
    public SimpleBBQBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(SimpleBBQRegistry.GRILL_BLOCK.get(), this.models().getExistingFile(RLUtils.createRL("block/grill")));
        this.simpleBlock(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get(),
                this.models().cube("skewering_table",
                        RLUtils.createRL("block/skewering_table_bottom"),
                        RLUtils.createVanillaRL("block/smooth_stone"),
                        RLUtils.createRL("block/skewering_table_front"),
                        RLUtils.createRL("block/skewering_table_front"),
                        RLUtils.createRL("block/skewering_table_side"),
                        RLUtils.createRL("block/skewering_table_side")
                ).texture("particle", RLUtils.createRL("block/skewering_table_front"))
        );
    }
}
