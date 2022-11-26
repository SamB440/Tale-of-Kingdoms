package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerKingdom {

    private BlockPos start, end;
    private final BlockPos origin;
    private final Map<KingdomPOI, BlockPos> poi;
    private KingdomTier tier;

    public PlayerKingdom(BlockPos origin) {
        this.origin = origin;
        this.poi = new HashMap<>();
        this.tier = KingdomTier.TIER_ONE;
    }

    public <T extends Entity> Optional<T> getKingdomEntity(World world, EntityType<T> type) {
        if (start == null || end == null) return Optional.empty();
        Box box = new Box(start, end);
        return world.getEntitiesByType(type, box, entity -> true).stream().findFirst();
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

    public BlockPos getStart() {
        return start;
    }

    public void setStart(BlockPos start) {
        this.start = start;
    }

    public BlockPos getEnd() {
        return end;
    }

    public void setEnd(BlockPos end) {
        this.end = end;
    }

    public void addPOI(KingdomPOI poi, BlockPos pos) {
        this.poi.put(poi, pos);
    }

    public BlockPos getPOIPos(KingdomPOI poi) {
        return this.poi.get(poi);
    }
}
