package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class PlayerKingdom {

    private final BlockPos origin;
    private final Map<KingdomPOI, BlockPos> poi;
    private KingdomTier tier;

    public PlayerKingdom(BlockPos origin) {
        this.origin = origin;
        this.poi = new HashMap<>();
        this.tier = KingdomTier.TIER_ONE;
    }

    public KingdomTier getTier() {
        return tier;
    }

    public void setTier(KingdomTier tier) {
        this.tier = tier;
    }

    public BlockPos getOrigin() {
        return this.origin;
    }

    public void addPOI(KingdomPOI poi, BlockPos pos) {
        this.poi.put(poi, pos);
    }

    public BlockPos getPOIPos(KingdomPOI poi) {
        return this.poi.get(poi);
    }
}
