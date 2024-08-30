package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BuyItemPacket(String itemName, int count, ShopParser.GUI type) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, BuyItemPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.string(128), BuyItemPacket::itemName,
            PacketCodecs.INTEGER, BuyItemPacket::count,
            PacketCodecs.indexed(i -> ShopParser.GUI.values()[i], ShopParser.GUI::ordinal), BuyItemPacket::type,
            BuyItemPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.BUY_ITEM;
    }
}
