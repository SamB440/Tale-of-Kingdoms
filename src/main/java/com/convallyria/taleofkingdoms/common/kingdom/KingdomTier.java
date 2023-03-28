package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;
import org.jetbrains.annotations.Nullable;

public enum KingdomTier implements EnumCodec.Values {
    TIER_ONE,
    TIER_TWO;

    public boolean isLowerThanOrEqual(@Nullable KingdomTier tier) {
        if (tier == null) return false;
        return this.ordinal() <= tier.ordinal();
    }

    public boolean isHigherThanOrEqual(@Nullable KingdomTier tier) {
        if (tier == null) return false;
        return this.ordinal() >= tier.ordinal();
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
