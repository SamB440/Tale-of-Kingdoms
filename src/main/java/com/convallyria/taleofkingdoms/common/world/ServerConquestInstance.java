package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.netty.buffer.Unpooled;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
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

    public ServerConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
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

    public void sync(ServerPlayerEntity player, boolean reset, ClientConnection connection) {
        if (reset) {
            this.setBankerCoins(player.getUuid(), 0);
            this.setCoins(player.getUuid(), 0);
            this.setFarmerLastBread(player.getUuid(), 0);
            this.setHasContract(player.getUuid(), false);
            this.setWorthiness(player.getUuid(), 0);
        }
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeString(this.getName());
        passedData.writeString(this.getWorld());
        passedData.writeInt(this.getBankerCoins(player.getUuid()));
        passedData.writeInt(this.getCoins(player.getUuid()));
        passedData.writeInt(this.getWorthiness(player.getUuid()));
        passedData.writeLong(this.getFarmerLastBread(player.getUuid()));
        passedData.writeBoolean(this.hasContract(player.getUuid()));
        passedData.writeBoolean(this.isLoaded());
        passedData.writeBlockPos(this.getStart());
        passedData.writeBlockPos(this.getEnd());
        passedData.writeBlockPos(this.getOrigin());
        // Then we'll send the packet to all the players
        TaleOfKingdoms.LOGGER.info("SENDING PACKET");
        System.out.println(player + " " + TaleOfKingdoms.INSTANCE_PACKET_ID + " " + passedData);
        if (connection != null) connection.send(new CustomPayloadS2CPacket(TaleOfKingdoms.INSTANCE_PACKET_ID, passedData));
        else player.networkHandler.connection.send(new CustomPayloadS2CPacket(TaleOfKingdoms.INSTANCE_PACKET_ID, passedData));
    }
}
