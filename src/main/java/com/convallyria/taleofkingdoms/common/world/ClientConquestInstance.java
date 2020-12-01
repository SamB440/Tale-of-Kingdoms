package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientConquestInstance extends ConquestInstance {

    private int coins;
    private int bankerCoins;
    private long farmerLastBread;
    private boolean hasContract;
    private int worthiness;
    private List<UUID> hunterUUIDs;

    public ClientConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
    }

    public int getCoins() {
        return coins;
    }

    public int getBankerCoins() { return bankerCoins; }

    public void setBankerCoins(int bankerCoins) { this.bankerCoins = bankerCoins; }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins = this.coins + coins;
    }

    public long getFarmerLastBread() {
        return farmerLastBread;
    }

    public void setFarmerLastBread(long day) {
        this.farmerLastBread = day;
    }

    public boolean hasContract() {
        return hasContract;
    }

    public void setHasContract(boolean hasContract) {
        this.hasContract = hasContract;
    }

    public int getWorthiness() {
        return worthiness;
    }

    public void setWorthiness(int worthiness) {
        this.worthiness = worthiness;
    }

    public void addWorthiness(int worthiness) {
        this.worthiness = this.worthiness + worthiness;
    }

    public void setHunterUUIDs(List<UUID> hunterUUIDs) {
        if(this.hunterUUIDs == null) this.hunterUUIDs = new ArrayList<>();
        this.hunterUUIDs = hunterUUIDs;
    }

    public List<UUID> getHunter() {
        if(this.hunterUUIDs == null) this.hunterUUIDs = new ArrayList<>();
        return hunterUUIDs;
    }

    public void addHunter(HunterEntity entity) {
        this.hunterUUIDs.add(entity.getUuid());
    }

    public void removeHunter(Entity entity) {
        this.hunterUUIDs.remove(entity.getUuid());
    }
}
