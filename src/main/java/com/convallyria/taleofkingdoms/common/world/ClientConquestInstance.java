package com.convallyria.taleofkingdoms.common.world;

import net.minecraft.util.math.BlockPos;

public class ClientConquestInstance extends ConquestInstance {

    private int coins;
    private int bankerCoins;
    private boolean hasLoaded;
    private long farmerLastBread;
    private boolean hasContract;
    private int worthiness;

    public ClientConquestInstance(String world, String name, BlockPos start, BlockPos end) {
        super(world, name, start, end);
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
}
