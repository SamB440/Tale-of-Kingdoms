package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ToggleSellGuiPacket(boolean close, ShopParser.GUI guiType) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleSellGuiPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ToggleSellGuiPacket::close,
            ByteBufCodecs.idMapper(i -> ShopParser.GUI.values()[i], ShopParser.GUI::ordinal), ToggleSellGuiPacket::guiType,
            ToggleSellGuiPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.TOGGLE_SELL_GUI;
    }
}
