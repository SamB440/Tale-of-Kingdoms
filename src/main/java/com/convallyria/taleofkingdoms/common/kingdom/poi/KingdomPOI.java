package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import net.minecraft.structure.StructureTemplate;

import java.util.Optional;

public enum KingdomPOI implements POIProcessor {
    CITY_BUILDER_WELL_POI("CityBuilderWellPOI") {
        @Override
        public void compute(PlayerKingdom kingdom, StructureTemplate.StructureBlockInfo info) {
            kingdom.addPOI(CITY_BUILDER_WELL_POI, info.pos);
            TaleOfKingdoms.LOGGER.info("Found city builder well POI @ " + info.pos);
        }
    };

    private final String poiName;

    KingdomPOI(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiName() {
        return poiName;
    }

    public static Optional<KingdomPOI> getFrom(String poiName) {
        for (KingdomPOI value : values()) {
            if (value.poiName.equals(poiName)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
