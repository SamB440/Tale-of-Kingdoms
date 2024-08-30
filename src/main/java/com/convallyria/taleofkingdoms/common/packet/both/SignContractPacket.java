package com.convallyria.taleofkingdoms.common.packet.both;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SignContractPacket(boolean signed) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, SignContractPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, SignContractPacket::signed,
            SignContractPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.SIGN_CONTRACT;
    }
}
