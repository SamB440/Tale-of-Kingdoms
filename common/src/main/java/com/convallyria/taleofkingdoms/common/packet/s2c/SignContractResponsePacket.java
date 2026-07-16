package com.convallyria.taleofkingdoms.common.packet.s2c;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Server -> client confirmation of a contract sign/cancel. This is a separate payload type from
 * {@link com.convallyria.taleofkingdoms.common.packet.c2s.SignContractPacket} because NeoForge
 * does not allow the same payload id to be registered C2S and S2C independently (it requires a
 * single bidirectional registration, which Architectury's NetworkManager does not expose).
 */
public record SignContractResponsePacket(boolean signed) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, SignContractResponsePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SignContractResponsePacket::signed,
            SignContractResponsePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.SIGN_CONTRACT_RESPONSE;
    }
}
