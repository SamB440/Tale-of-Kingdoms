package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructureTemplate;

import java.util.Optional;

public enum KingdomPOI {
    CITY_BUILDER_WELL_POI("CityBuilderWellPOI"),

    // Blacksmith / Item Shop house
    TIER_ONE_HOUSE_BLACKSMITH("TierOneHouseBlacksmith"),
    TIER_ONE_BLACKSMITH("TierOneBlacksmith") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.BLACKSMITH, player, info.pos);
        }
    },
    TIER_ONE_ITEM_SHOP("TierOneItemShop") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.ITEM_SHOP, player, info.pos);
        }
    },

    TIER_ONE_STOCK_MARKET("TierOneStockMarket"),

    TIER_ONE_SMALL_HOUSE_1("TierOneSmallHouse1"),
    TIER_ONE_SMALL_HOUSE_2("TierOneSmallHouse2"),
    TIER_ONE_LARGE_HOUSE("TierOneLargeHouse"),

    KINGDOM_VILLAGER("KingdomVillager") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.KINGDOM_VILLAGER, player, info.pos);
        }
    },
    STOCK_MARKET_ENTITY("StockMarketEntity") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.STOCK_MARKET, player, info.pos);
        }
    },

    QUARRY_FOREMAN("QuarryForeman") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.QUARRY_FOREMAN, player, info.pos);
        }
    };

    private final String poiName;

    KingdomPOI(String poiName) {
        this.poiName = poiName;
    }

    public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
        kingdom.addPOI(this, info.pos);
        TaleOfKingdoms.LOGGER.info("Found '" + poiName + "' POI @ " + info.pos);
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
