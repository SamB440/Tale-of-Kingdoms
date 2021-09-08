package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingBankerInteractPacketHandler extends ClientPacketHandler {

    public OutgoingBankerInteractPacketHandler() {
        super(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeEnumConstant((BankerMethod) data[0]);
        passedData.writeInt((Integer) data[1]);
        sendPacket(player, passedData);
    }
}
