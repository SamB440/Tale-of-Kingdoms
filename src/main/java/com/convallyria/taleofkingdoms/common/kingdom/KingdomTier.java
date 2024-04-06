package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

public enum KingdomTier implements EnumCodec.Values {
    TIER_ONE(Text.translatable("menu.taleofkingdoms.generic.tier_one"), Schematic.TIER_1_KINGDOM, Vec3i.ZERO),
    TIER_TWO(Text.translatable("menu.taleofkingdoms.generic.tier_two"), Schematic.TIER_2_KINGDOM, new Vec3i(16, 0, 49));

    private final Text name;
    private final Schematic schematic;
    private final Vec3i offset;

    KingdomTier(Text name, Schematic schematic, Vec3i offset) {
        this.name = name;
        this.schematic = schematic;
        this.offset = offset;
    }

    public Text getName() {
        return name;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public Vec3i getOffset() {
        return offset;
    }

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
