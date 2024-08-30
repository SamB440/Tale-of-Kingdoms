package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ToggleSellGuiPacket(boolean close, ShopParser.GUI type) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, ToggleSellGuiPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, ToggleSellGuiPacket::close,
            PacketCodecs.indexed(i -> ShopParser.GUI.values()[i], ShopParser.GUI::ordinal), ToggleSellGuiPacket::type,
            ToggleSellGuiPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.TOGGLE_SELL_GUI;
    }
}
