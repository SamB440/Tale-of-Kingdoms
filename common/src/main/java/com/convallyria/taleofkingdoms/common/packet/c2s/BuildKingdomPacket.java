package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BuildKingdomPacket(int entityId) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, BuildKingdomPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BuildKingdomPacket::entityId,
            BuildKingdomPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return Packets.BUILD_KINGDOM;
    }
}
