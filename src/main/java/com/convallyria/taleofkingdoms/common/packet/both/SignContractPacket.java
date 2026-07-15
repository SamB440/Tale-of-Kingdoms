package com.convallyria.taleofkingdoms.common.packet.both;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SignContractPacket(boolean signed) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, SignContractPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SignContractPacket::signed,
            SignContractPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.SIGN_CONTRACT;
    }
}
