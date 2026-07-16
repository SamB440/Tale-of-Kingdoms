package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HireHunterPacket(boolean retire) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, HireHunterPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HireHunterPacket::retire,
            HireHunterPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.HIRE_HUNTER;
    }
}
