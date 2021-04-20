package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientConquestInstance extends ConquestInstance {

    private int coins;
    private int bankerCoins;
    private long farmerLastBread;
    private boolean hasContract;
    private int worthiness;
    private List<UUID> hunterUUIDs;

    public ClientConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
        this.hunterUUIDs = new ArrayList<>();
    }

    @Override
    public int getCoins(UUID uuid) {
        return coins;
    }

    @Override
    public int getBankerCoins(UUID uuid) { return bankerCoins; }

    @Override
    public void setBankerCoins(UUID uuid, int bankerCoins) { this.bankerCoins = bankerCoins; }

    @Override
    public void setCoins(UUID uuid, int coins) {
        this.coins = coins;
    }

    @Override
    public void addCoins(UUID uuid, int coins) {
        this.coins = this.coins + coins;
    }

    @Override
    public long getFarmerLastBread(UUID uuid) {
        return farmerLastBread;
    }

    @Override
    public void setFarmerLastBread(UUID uuid, long day) {
        this.farmerLastBread = day;
    }

    @Override
    public boolean hasContract(UUID uuid) {
        return hasContract;
    }

    @Override
    public void setHasContract(UUID uuid, boolean hasContract) {
        this.hasContract = hasContract;
    }

    @Override
    public int getWorthiness(UUID uuid) {
        return worthiness;
    }

    @Override
    public void setWorthiness(UUID uuid, int worthiness) {
        this.worthiness = worthiness;
    }

    @Override
    public void addWorthiness(UUID uuid, int worthiness) {
        this.worthiness = this.worthiness + worthiness;
    }

    public void addHunter(Entity entity) {
        this.hunterUUIDs.add(entity.getUuid());
    }

    public ImmutableList<UUID> getHunterUUIDs() {
        if (hunterUUIDs == null) this.hunterUUIDs = new ArrayList<>();
        return ImmutableList.copyOf(hunterUUIDs);
    }

    public void removeHunter(Entity entity) {
        this.hunterUUIDs.remove(entity.getUuid());
    }

    public CompletableFuture<BlockBox> rebuild(ServerPlayerEntity serverPlayerEntity, TaleOfKingdomsAPI api, boolean entities) {
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayerEntity, getOrigin().subtract(new Vec3i(0, 13, 0)));
    }
}
