package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record InnkeeperActionPacket(boolean resting) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, InnkeeperActionPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, InnkeeperActionPacket::resting,
            InnkeeperActionPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.INNKEEPER_HIRE_ROOM;
    }
}
