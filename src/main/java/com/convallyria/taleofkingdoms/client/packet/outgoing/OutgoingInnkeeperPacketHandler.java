package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingInnkeeperPacketHandler extends ClientPacketHandler {

    public OutgoingInnkeeperPacketHandler() {
        super(TaleOfKingdoms.INNKEEPER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBoolean((Boolean) data[0]); // True if resting, false if waiting to night
        sendPacket(connection, passedData);
    }
}
