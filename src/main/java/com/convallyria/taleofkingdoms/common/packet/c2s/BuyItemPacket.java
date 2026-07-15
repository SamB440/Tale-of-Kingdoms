package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BuyItemPacket(String itemName, int count, ShopParser.GUI guiType) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, BuyItemPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(128), BuyItemPacket::itemName,
            ByteBufCodecs.INT, BuyItemPacket::count,
            ByteBufCodecs.idMapper(i -> ShopParser.GUI.values()[i], ShopParser.GUI::ordinal), BuyItemPacket::guiType,
            BuyItemPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.BUY_ITEM;
    }
}
