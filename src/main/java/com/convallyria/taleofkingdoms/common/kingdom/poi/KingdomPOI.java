package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum KingdomPOI {
    CITY_BUILDER_WELL_POI("CityBuilderWellPOI"),
    TIER_ONE_HOUSE_BLACKSMITH("TierOneHouseBlacksmith");

    private final String poiName;
    private final Schematic schematic;

    KingdomPOI(String poiName) {
        this(poiName, null);
    }

    KingdomPOI(String poiName, @Nullable Schematic schematic) {
        this.poiName = poiName;
        this.schematic = schematic;
    }

    public void compute(PlayerKingdom kingdom, StructureTemplate.StructureBlockInfo info) {
        kingdom.addPOI(this, info.pos);
        TaleOfKingdoms.LOGGER.info("Found '" + poiName + "' POI @ " + info.pos);
    }

    public void place(ServerPlayerEntity player, PlayerKingdom kingdom) {
        if (schematic != null) {
            TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(schematic, player, kingdom.getPOIPos(this));
        }
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
