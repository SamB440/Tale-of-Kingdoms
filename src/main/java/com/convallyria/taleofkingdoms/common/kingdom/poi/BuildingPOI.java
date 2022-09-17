package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import net.minecraft.structure.StructureTemplate;

import java.util.Optional;

public enum BuildingPOI implements POIProcessor {
    SMALL_HOUSE_1("SmallHouse1POI") {
        @Override
        public void compute(PlayerKingdom kingdom, StructureTemplate.StructureBlockInfo info) {
            //todo
        }
    };

    private final String poiName;

    BuildingPOI(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiName() {
        return poiName;
    }

    public static Optional<BuildingPOI> getFrom(String poiName) {
        for (BuildingPOI value : values()) {
            if (value.poiName.equals(poiName)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
