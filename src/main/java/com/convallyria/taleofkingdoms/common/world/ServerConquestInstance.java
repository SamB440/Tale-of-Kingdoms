package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConquestInstance extends ConquestInstance {

    private final Map<UUID, Integer> playerCoins;
    private final Map<UUID, Integer> playerBankerCoins;
    private final Map<UUID, Long> playerFarmerLastBread;
    private final Map<UUID, Boolean> playerHasContract;
    private final Map<UUID, Integer> playerWorthiness;
    private Map<UUID, List<UUID>> hunterUUIDs;

    public ServerConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
        this.playerCoins = new ConcurrentHashMap<>();
        this.playerBankerCoins = new ConcurrentHashMap<>();
        this.playerFarmerLastBread = new ConcurrentHashMap<>();
        this.playerHasContract = new ConcurrentHashMap<>();
        this.playerWorthiness = new ConcurrentHashMap<>();
        this.hunterUUIDs = new ConcurrentHashMap<>();
    }

    public boolean hasPlayer(UUID playerUuid) {
        return playerCoins.containsKey(playerUuid)
                && playerBankerCoins.containsKey(playerUuid)
                && playerFarmerLastBread.containsKey(playerUuid)
                && playerHasContract.containsKey(playerUuid)
                && playerWorthiness.containsKey(playerUuid);
    }

    @Override
    public int getCoins(UUID uuid) {
        return playerCoins.get(uuid);
    }

    @Override
    public int getBankerCoins(UUID uuid) { return playerBankerCoins.get(uuid); }

    @Override
    public void setBankerCoins(UUID uuid, int bankerCoins) { this.playerBankerCoins.put(uuid, bankerCoins); }

    @Override
    public void setCoins(UUID uuid, int coins) {
        this.playerCoins.put(uuid, coins);
    }

    @Override
    public void addCoins(UUID uuid, int coins) {
        this.playerCoins.put(uuid, this.playerCoins.get(uuid) + coins);
    }

    @Override
    public long getFarmerLastBread(UUID uuid) {
        return playerFarmerLastBread.get(uuid);
    }

    @Override
    public void setFarmerLastBread(UUID uuid, long day) {
        this.playerFarmerLastBread.put(uuid, day);
    }

    @Override
    public boolean hasContract(UUID uuid) {
        return playerHasContract.get(uuid);
    }

    @Override
    public void setHasContract(UUID uuid, boolean hasContract) {
        this.playerHasContract.put(uuid, hasContract);
    }

    @Override
    public int getWorthiness(UUID playerUuid) {
        return playerWorthiness.get(playerUuid);
    }

    @Override
    public void setWorthiness(UUID playerUuid, int worthiness) {
        this.playerWorthiness.put(playerUuid, worthiness);
    }

    @Override
    public void addWorthiness(UUID playerUuid, int worthiness) {
        this.playerWorthiness.put(playerUuid, this.playerWorthiness.get(playerUuid) + worthiness);
    }

    public Map<UUID, List<UUID>> getHunterUUIDs() {
        if (hunterUUIDs == null) hunterUUIDs = new ConcurrentHashMap<>();
        return hunterUUIDs;
    }

    public void addHunter(UUID playerUuid, HunterEntity hunterEntity) {
        List<UUID> uuids = hunterUUIDs.getOrDefault(playerUuid, new ArrayList<>());
        uuids.add(hunterEntity.getUUID());
        hunterUUIDs.put(playerUuid, uuids);
    }

    public void removeHunter(UUID playerUuid, UUID hunterUuid) {
        List<UUID> uuids = hunterUUIDs.getOrDefault(playerUuid, new ArrayList<>());
        uuids.remove(hunterUuid);
        hunterUUIDs.put(playerUuid, uuids);
    }

    public void reset(@NotNull ServerPlayer player) {
        this.setBankerCoins(player.getUUID(), 0);
        this.setCoins(player.getUUID(), 0);
        this.setFarmerLastBread(player.getUUID(), 0);
        this.setHasContract(player.getUUID(), false);
        this.setWorthiness(player.getUUID(), 0);
    }

    public void sync(@NotNull ServerPlayer player, @Nullable Connection connection) {
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            PacketHandler packetHandler = api.getServerHandler(TaleOfKingdoms.INSTANCE_PACKET_ID);
            packetHandler.handleOutgoingPacket(TaleOfKingdoms.INSTANCE_PACKET_ID, player, connection, this);
        });
    }
}
