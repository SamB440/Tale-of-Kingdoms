package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;

public enum KingdomTier implements EnumCodec.Values {
    TIER_ONE;

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
