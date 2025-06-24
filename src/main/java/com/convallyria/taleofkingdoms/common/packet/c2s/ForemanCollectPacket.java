package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ForemanCollectPacket(int entityId) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, ForemanCollectPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ForemanCollectPacket::entityId,
            ForemanCollectPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.FOREMAN_COLLECT;
    }
}
