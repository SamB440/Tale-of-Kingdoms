package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FixGuildPacket() implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, FixGuildPacket> CODEC = StreamCodec.unit(new FixGuildPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.FIX_GUILD;
    }
}
