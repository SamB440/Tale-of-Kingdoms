package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConquestInstance extends ConquestInstance {

    private final Map<UUID, Integer> playerCoins;
    private final Map<UUID, Integer> playerBankerCoins;
    private final Map<UUID, Long> playerFarmerLastBread;
    private final Map<UUID, Boolean> playerHasContract;
    private final Map<UUID, Integer> playerWorthiness;

    public ServerConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
        this.playerCoins = new ConcurrentHashMap<>();
        this.playerBankerCoins = new ConcurrentHashMap<>();
        this.playerFarmerLastBread = new ConcurrentHashMap<>();
        this.playerHasContract = new ConcurrentHashMap<>();
        this.playerWorthiness = new ConcurrentHashMap<>();
    }

    public boolean hasPlayer(UUID playerUuid) {
        return playerCoins.containsKey(playerUuid)
                && playerBankerCoins.containsKey(playerUuid)
                && playerFarmerLastBread.containsKey(playerUuid)
                && playerHasContract.containsKey(playerUuid)
                && playerWorthiness.containsKey(playerUuid);
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

    public void reset(@NotNull ServerPlayerEntity player) {
        this.setBankerCoins(player.getUuid(), 0);
        this.setCoins(player.getUuid(), 0);
        this.setFarmerLastBread(player.getUuid(), 0);
        this.setHasContract(player.getUuid(), false);
        this.setWorthiness(player.getUuid(), 0);
    }

    public void sync(@NotNull ServerPlayerEntity player, @Nullable ClientConnection connection) {
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            PacketHandler packetHandler = api.getServerHandler(TaleOfKingdoms.INSTANCE_PACKET_ID);
            packetHandler.handleOutgoingPacket(TaleOfKingdoms.INSTANCE_PACKET_ID, player, connection, this);
        });
    }
}
