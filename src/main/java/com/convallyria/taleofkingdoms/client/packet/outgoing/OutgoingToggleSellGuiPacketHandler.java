package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingToggleSellGuiPacketHandler extends ClientPacketHandler {

    public OutgoingToggleSellGuiPacketHandler() {
        super(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBoolean((Boolean) data[0]);
        if (connection == null) MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(identifier, passedData));
        else connection.send(new CustomPayloadC2SPacket(identifier, passedData));
    }
}
