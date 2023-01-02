package com.convallyria.taleofkingdoms.common.world.data;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConquestDataConverter1 extends DataConverter {
    @Override
    public void convert(ConquestInstance instance) {
        instance.lastStockMarketUpdate = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> getVersions() {
        return List.of("1.0.0", "1.0.1", "1.0.2", "1.0.3");
    }
}
