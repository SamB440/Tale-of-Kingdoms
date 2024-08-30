package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record InnkeeperActionPacket(boolean resting) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, InnkeeperActionPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, InnkeeperActionPacket::resting,
            InnkeeperActionPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.INNKEEPER_HIRE_ROOM;
    }
}
