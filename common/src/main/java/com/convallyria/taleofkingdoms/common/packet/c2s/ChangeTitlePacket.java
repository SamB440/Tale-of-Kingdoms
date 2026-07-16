package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ChangeTitlePacket(PlayerTitle title) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeTitlePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(i -> PlayerTitle.values()[i], PlayerTitle::ordinal), ChangeTitlePacket::title,
            ChangeTitlePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.CHANGE_TITLE;
    }
}
