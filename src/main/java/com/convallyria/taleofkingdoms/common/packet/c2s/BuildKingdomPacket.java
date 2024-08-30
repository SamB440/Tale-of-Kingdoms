package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BuildKingdomPacket(int entityId) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, BuildKingdomPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, BuildKingdomPacket::entityId,
            BuildKingdomPacket::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return Packets.BUILD_KINGDOM;
    }
}
