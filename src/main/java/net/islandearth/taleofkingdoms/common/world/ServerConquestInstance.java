package net.islandearth.taleofkingdoms.common.world;

import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConquestInstance extends ConquestInstance {

    private Map<UUID, Integer> playerCoins;
    private Map<UUID, Integer> playerBankerCoins;
    private Map<UUID, Long> playerFarmerLastBread;
    private Map<UUID, Boolean> playerHasContract;
    private Map<UUID, Integer> playerWorthiness;

    public ServerConquestInstance(String world, String name, BlockPos start, BlockPos end) {
        super(world, name, start, end);
        this.playerCoins = new ConcurrentHashMap<>();
        this.playerBankerCoins = new ConcurrentHashMap<>();
        this.playerFarmerLastBread = new ConcurrentHashMap<>();
        this.playerHasContract = new ConcurrentHashMap<>();
        this.playerWorthiness = new ConcurrentHashMap<>();
    }

    public int getCoins(UUID playerUuid) {
        return playerCoins.get(playerUuid);
    }

    public int getBankerCoins(UUID playerUuid) { return playerBankerCoins.get(playerUuid); }

    public void setBankerCoins(UUID playerUuid, int bankerCoins) { this.playerBankerCoins.put(playerUuid, bankerCoins); }

    public void setCoins(UUID playerUuid, int coins) {
        this.playerCoins.put(playerUuid, coins);
    }

    public void addCoins(UUID playerUuid, int coins) {
        this.playerCoins.put(playerUuid, this.playerCoins.get(playerUuid) + coins);
    }

    public long getFarmerLastBread(UUID playerUuid) {
        return playerFarmerLastBread.get(playerUuid);
    }

    public void setFarmerLastBread(UUID playerUuid, long day) {
        this.playerFarmerLastBread.put(playerUuid, day);
    }

    public boolean hasContract(UUID playerUuid) {
        return playerHasContract.get(playerUuid);
    }

    public void setHasContract(UUID playerUuid, boolean hasContract) {
        this.playerHasContract.put(playerUuid, hasContract);
    }

    public int getWorthiness(UUID playerUuid) {
        return playerWorthiness.get(playerUuid);
    }

    public void setWorthiness(UUID playerUuid, int worthiness) {
        this.playerWorthiness.put(playerUuid, worthiness);
    }

    public void addWorthiness(UUID playerUuid, int worthiness) {
        this.playerWorthiness.put(playerUuid, this.playerWorthiness.get(playerUuid) + worthiness);
    }
}
