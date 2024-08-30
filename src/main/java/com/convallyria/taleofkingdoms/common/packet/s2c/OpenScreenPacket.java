package com.convallyria.taleofkingdoms.common.packet.s2c;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OpenScreenPacket(ScreenTypes type, int entityId) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, OpenScreenPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.indexed(i -> ScreenTypes.values()[i], ScreenTypes::ordinal), OpenScreenPacket::type,
            PacketCodecs.INTEGER, OpenScreenPacket::entityId,
            OpenScreenPacket::new);

    public enum ScreenTypes {
        GUILD_MASTER,
        BLACKSMITH,
        INNKEEPER,
        BANKER,
        FOOD_SHOP,
        ITEM_SHOP,
        BLOCK_SHOP,
        CITY_BUILDER_BEGIN,
        CITY_BUILDER_TIER,
        STOCK_MARKET,
        FOREMAN,
        WARDEN
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.OPEN_CLIENT_SCREEN;
    }
}
