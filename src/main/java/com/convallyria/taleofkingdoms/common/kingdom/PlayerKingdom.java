package com.convallyria.taleofkingdoms.common.kingdom;

import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerKingdom {

    public static final Codec<PlayerKingdom> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockPos.CODEC.fieldOf("start").forGetter(PlayerKingdom::getStart),
                    BlockPos.CODEC.fieldOf("end").forGetter(PlayerKingdom::getEnd),
                    BlockPos.CODEC.fieldOf("origin").forGetter(PlayerKingdom::getOrigin),
                    Codec.unboundedMap(new EnumCodec<>(KingdomPOI.class), BlockPos.CODEC).fieldOf("poi").forGetter(PlayerKingdom::getPoi),
                    new EnumCodec<>(BuildCosts.class).listOf().fieldOf("built_buildings").forGetter(PlayerKingdom::getBuiltBuildings),
                    new EnumCodec<>(KingdomTier.class).fieldOf("tier").forGetter(PlayerKingdom::getTier),
                    Codec.LONG.fieldOf("last_stock_market_update").forGetter(PlayerKingdom::getLastStockMarketUpdate),
                    Codec.LONG.fieldOf("last_tax_collection").forGetter(PlayerKingdom::getLastTaxCollection)
            ).apply(instance, (start, end, origin, poi, builtBuildings, tier, lastStockMarketUpdate, lastTaxCollection) -> {
                final PlayerKingdom playerKingdom = new PlayerKingdom(origin);
                playerKingdom.setStart(start);
                playerKingdom.setEnd(end);
                poi.forEach(playerKingdom::addPOI);
                builtBuildings.forEach(playerKingdom::addBuilt);
                playerKingdom.setTier(tier);
                playerKingdom.setLastStockMarketUpdate(lastStockMarketUpdate);
                playerKingdom.lastTaxCollection = lastTaxCollection;
                return playerKingdom;
            }
    ));

    private BlockPos start, end;
    private final BlockPos origin;
    private final Map<KingdomPOI, BlockPos> poi;
    private final List<BuildCosts> builtBuildings;
    private KingdomTier tier;
    private long lastStockMarketUpdate, lastTaxCollection;

    public PlayerKingdom(BlockPos origin) {
        this.origin = origin;
        this.poi = new HashMap<>();
        this.builtBuildings = new ArrayList<>();
        this.tier = KingdomTier.TIER_ONE;
    }

    public <T extends Entity> Optional<T> getKingdomEntity(World world, EntityType<T> type) {
        if (start == null || end == null) return Optional.empty();
        Box box = Box.enclosing(start, end);
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

    public Map<KingdomPOI, BlockPos> getPoi() {
        return poi;
    }

    public void addPOI(KingdomPOI poi, BlockPos pos) {
        this.poi.put(poi, pos);
    }

    public BlockPos getPOIPos(KingdomPOI poi) {
        return this.poi.get(poi);
    }

    public List<BuildCosts> getBuiltBuildings() {
        return builtBuildings;
    }

    public boolean hasBuilt(BuildCosts poi) {
        return this.builtBuildings.contains(poi);
    }

    public void addBuilt(BuildCosts poi) {
        this.builtBuildings.add(poi);
    }

    public long getLastStockMarketUpdate() {
        return lastStockMarketUpdate;
    }

    public void setLastStockMarketUpdate(long lastStockMarketUpdate) {
        this.lastStockMarketUpdate = lastStockMarketUpdate;
    }

    public long getLastTaxCollection() {
        return lastTaxCollection;
    }

    public int tryTaxCollection(GuildPlayer benefitter) {
        final long currentTime = System.currentTimeMillis();
        final long timeSince = currentTime - lastTaxCollection;
        // If less than an hour, don't tax
        if (timeSince < 3600000) return 0;

        int totalGold = 0;
        for (BuildCosts builtBuilding : builtBuildings) {
            if (builtBuilding == BuildCosts.SMALL_HOUSE_1 || builtBuilding == BuildCosts.SMALL_HOUSE_2) {
                totalGold += 50;
                continue;
            }

            if (builtBuilding == BuildCosts.LARGE_HOUSE) {
                totalGold += 125;
                continue;
            }

            if (builtBuilding == BuildCosts.TIER_2_SMALL_HOUSE_1 || builtBuilding == BuildCosts.TIER_2_SMALL_HOUSE_2) {
                totalGold += 175;
                continue;
            }

            if (builtBuilding == BuildCosts.TIER_2_LARGE_HOUSE) {
                totalGold += 250;
            }
        }

        benefitter.setCoins(benefitter.getCoins() + totalGold);
        this.lastTaxCollection = currentTime;
        return totalGold;
    }

    public boolean isInKingdom(BlockPos pos) {
        if (start == null || end == null) return false; // Probably still pasting.
        BlockBox blockBox = new BlockBox(end.getX(), end.getY(), end.getZ(), start.getX(), start.getY(), start.getZ());
        return blockBox.contains(pos);
    }
}
