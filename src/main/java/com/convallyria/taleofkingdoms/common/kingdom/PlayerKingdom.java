package com.convallyria.taleofkingdoms.common.kingdom;

import net.minecraft.util.math.BlockPos;

public class PlayerKingdom {

    private final BlockPos origin;
    private KingdomTier tier;

    public PlayerKingdom(BlockPos origin) {
        this.origin = origin;
        this.tier = KingdomTier.TIER_ONE;
    }

    public KingdomTier getTier() {
        return tier;
    }

    public void setTier(KingdomTier tier) {
        this.tier = tier;
    }

    public BlockPos getOrigin() {
        return origin;
    }
}
