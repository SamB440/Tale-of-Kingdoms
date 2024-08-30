package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record FixGuildPacket() implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, FixGuildPacket> CODEC = PacketCodec.unit(new FixGuildPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.FIX_GUILD;
    }
}
