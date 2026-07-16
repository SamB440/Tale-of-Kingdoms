package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum KingdomTier {
    TIER_ONE(Component.translatable("menu.taleofkingdoms.generic.tier_one"), Schematic.TIER_1_KINGDOM, Vec3i.ZERO),
    TIER_TWO(Component.translatable("menu.taleofkingdoms.generic.tier_two"), Schematic.TIER_2_KINGDOM, new Vec3i(16, 0, 49));

    private final Component name;
    private final Schematic schematic;
    private final Vec3i offset;

    KingdomTier(Component name, Schematic schematic, Vec3i offset) {
        this.name = name;
        this.schematic = schematic;
        this.offset = offset;
    }

    public Component getName() {
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
}
