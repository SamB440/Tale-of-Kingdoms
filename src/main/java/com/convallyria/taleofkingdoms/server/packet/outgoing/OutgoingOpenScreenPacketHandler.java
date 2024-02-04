package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingOpenScreenPacketHandler extends ServerPacketHandler {

    public enum ScreenTypes {
        GUILD_MASTER,
        BLACKSMITH,
        INNKEEPER,
        BANKER,
        FOOD_SHOP,
        ITEM_SHOP,
        CITY_BUILDER_BEGIN,
        CITY_BUILDER_TIER_ONE,
        STOCK_MARKET,
        FOREMAN
    }

    public OutgoingOpenScreenPacketHandler() {
        super(Packets.OPEN_CLIENT_SCREEN);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && player instanceof ServerPlayerEntity) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeEnumConstant((ScreenTypes) data[0]);
            passedData.writeInt((Integer) data[1]);
            sendPacket(player, passedData);
        }
    }
}
