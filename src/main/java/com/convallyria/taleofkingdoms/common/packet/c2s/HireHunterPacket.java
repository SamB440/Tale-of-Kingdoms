package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record HireHunterPacket(boolean retire) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, HireHunterPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, HireHunterPacket::retire,
            HireHunterPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.HIRE_HUNTER;
    }
}
