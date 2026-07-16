package com.convallyria.taleofkingdoms.common.packet.s2c;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record OpenScreenPacket(ScreenTypes screenType, int entityId) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreenPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(i -> ScreenTypes.values()[i], ScreenTypes::ordinal), OpenScreenPacket::screenType,
            ByteBufCodecs.INT, OpenScreenPacket::entityId,
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
    public Type<? extends CustomPacketPayload> type() {
        return Packets.OPEN_CLIENT_SCREEN;
    }
}
