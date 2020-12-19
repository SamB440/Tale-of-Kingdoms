package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OutgoingInstanceSyncPacketHandler extends ServerPacketHandler {

    public OutgoingInstanceSyncPacketHandler() {
        super(TaleOfKingdoms.INSTANCE_PACKET_ID);
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull ServerPlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        if (data != null && data[0] instanceof ServerConquestInstance) {
            ServerConquestInstance instance = (ServerConquestInstance) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeString(instance.getName());
            passedData.writeString(instance.getWorld());
            passedData.writeInt(instance.getBankerCoins(player.getUuid()));
            passedData.writeInt(instance.getCoins(player.getUuid()));
            passedData.writeInt(instance.getWorthiness(player.getUuid()));
            passedData.writeLong(instance.getFarmerLastBread(player.getUuid()));
            passedData.writeBoolean(instance.hasContract(player.getUuid()));
            passedData.writeBoolean(instance.isLoaded());
            passedData.writeBlockPos(instance.getStart());
            passedData.writeBlockPos(instance.getEnd());
            passedData.writeBlockPos(instance.getOrigin());
            // Then we'll send the packet to all the players
            TaleOfKingdoms.LOGGER.info("SENDING PACKET");
            System.out.println(player + " " + TaleOfKingdoms.INSTANCE_PACKET_ID + " " + passedData);
            if (connection != null) connection.send(new CustomPayloadS2CPacket(identifier, passedData));
            else player.networkHandler.connection.send(new CustomPayloadS2CPacket(identifier, passedData));
        }
    }
}
