package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingFixGuildPacketHandler extends ClientPacketHandler {

    public OutgoingFixGuildPacketHandler() {
        super(TaleOfKingdoms.FIX_GUILD_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        sendPacket(player, passedData);
    }
}
