package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public enum KingdomPOI implements EnumCodec.Values {
    CITY_BUILDER_WELL_POI("CityBuilderWellPOI") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            BlockPos pos = info.pos();
            if (kingdom.hasBuilt(BuildCosts.BUILDER_HOUSE)) {
                final BlockPos poiPos = kingdom.getPOIPos(KingdomPOI.CITY_BUILDER_HOUSE_POI);
                if (poiPos != null) {
                    pos = poiPos;
                }
            }

            BlockPos finalPos = pos;
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.CITYBUILDER).ifPresent(entity -> {
                entity.requestTeleport(finalPos.getX(), finalPos.getY(), finalPos.getZ());
                entity.setTarget(finalPos);
                TaleOfKingdoms.LOGGER.info("Teleported city builder to " + finalPos);
            });
        }
    },

    CITY_BUILDER_HOUSE_POI("CityBuilderHousePOI") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.CITYBUILDER).ifPresent(entity -> entity.setTarget(info.pos()));
        }
    },

    // Blacksmith / Item Shop house
    TIER_ONE_HOUSE_BLACKSMITH("TierOneHouseBlacksmith"),
    TIER_ONE_BLACKSMITH("TierOneBlacksmith") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.BLACKSMITH).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.BLACKSMITH, player, info.pos()));
        }
    },
    TIER_ONE_ITEM_SHOP("TierOneItemShop") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.ITEM_SHOP).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.ITEM_SHOP, player, info.pos()));
        }
    },

    TIER_ONE_STOCK_MARKET("TierOneStockMarket"),

    TIER_ONE_SMALL_HOUSE_1("TierOneSmallHouse1"),
    TIER_ONE_SMALL_HOUSE_2("TierOneSmallHouse2"),
    TIER_ONE_LARGE_HOUSE("TierOneLargeHouse"),

    TIER_TWO_SMALL_HOUSE_VARIANT_ONE("TierTwoSmallHouseVariantOne"),
    TIER_TWO_SMALL_HOUSE_VARIANT_TWO("TierTwoSmallHouseVariantTwo"),
    TIER_TWO_LARGE_HOUSE("TierTwoLargeHouse"),
    TIER_TWO_BARRACKS("TierTwoBarracks"),
    TIER_TWO_BAKE_HOUSE("TierTwoBakeHouse"),
    TIER_TWO_BUILDER_HOUSE("TierTwoBuilderHouse"),
    TIER_TWO_BLOCK_SHOP("TierTwoBlockShop"),

    KINGDOM_VILLAGER("KingdomVillager") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            EntityUtils.spawnEntity(EntityTypes.KINGDOM_VILLAGER, player, info.pos());
        }
    },
    STOCK_MARKET_ENTITY("StockMarketEntity") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.STOCK_MARKET).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.STOCK_MARKET, player, info.pos()));
        }
    },

    QUARRY_FOREMAN("QuarryForeman") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.QUARRY_FOREMAN).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.QUARRY_FOREMAN, player, info.pos()));
        }
    },

    LUMBER_FOREMAN("LumberForeman") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.LUMBER_FOREMAN).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.LUMBER_FOREMAN, player, info.pos()));
        }
    },

    QUARRY_WORKER_SPAWN("QuarryWorker"),
    LUMBER_WORKER_SPAWN("lumberworker"),

    WARDEN("Warden") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.WARDEN).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.WARDEN, player, info.pos()));
        }
    },

    FOODSHOP("FoodShop") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.FOODSHOP).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.FOODSHOP, player, info.pos()));
        }
    },

    BLOCKSHOP("BlockShop") {
        @Override
        public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
            super.compute(kingdom, player, info);
            kingdom.getKingdomEntity(player.getWorld(), EntityTypes.BLOCK_SHOP).ifPresentOrElse(entity -> {
                entity.requestTeleport(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            }, () -> EntityUtils.spawnEntity(EntityTypes.BLOCK_SHOP, player, info.pos()));
        }
    };

    private final String poiName;

    KingdomPOI(String poiName) {
        this.poiName = poiName;
    }

    public void compute(PlayerKingdom kingdom, ServerPlayerEntity player, StructureTemplate.StructureBlockInfo info) {
        kingdom.addPOI(this, info.pos());
        TaleOfKingdoms.LOGGER.info("Found '" + poiName + "' POI @ " + info.pos());
    }

    public boolean hasEntity(PlayerKingdom kingdom, ServerPlayerEntity player, EntityType<? extends TOKEntity> entity) {
        return kingdom.getKingdomEntity(player.getWorld(), entity).isPresent();
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

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
