package com.convallyria.taleofkingdoms.common.datafixer;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.datafixer.schemas.Version1;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;

public class Schemas {

    public static final DataFixer FIXER = Schemas.create();
    public static final int CURRENT_VERSION = TaleOfKingdoms.DATA_FORMAT_VERSION;

    private Schemas() {}

    private static DataFixer create() {
        TaleOfKingdoms.LOGGER.info("Loading data fixer version %d", CURRENT_VERSION);
        DataFixerBuilder dataFixerBuilder = new DataFixerBuilder(CURRENT_VERSION);
        build(dataFixerBuilder);

        boolean asyncOptimized = false;
        TaleOfKingdoms.LOGGER.info("Building %s datafixer", asyncOptimized ? "optimized" : "unoptimized");

        // Let's copy LazyDFU and use DataFixerBuilder#buildUnoptimized.
        return dataFixerBuilder.buildUnoptimized();
    }

    private static void build(DataFixerBuilder builder) {
        TaleOfKingdoms.LOGGER.info("Loading schemas");
        builder.addSchema(1, Version1::new);

        TaleOfKingdoms.LOGGER.info("Loading fixers");
        // We don't have anything to DFU... yet!
    }
}