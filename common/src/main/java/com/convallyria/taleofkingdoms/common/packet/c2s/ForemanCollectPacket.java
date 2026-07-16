package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ForemanCollectPacket(int entityId) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ForemanCollectPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ForemanCollectPacket::entityId,
            ForemanCollectPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.FOREMAN_COLLECT;
    }
}
