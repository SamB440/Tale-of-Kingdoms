package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @param signed whether the contract is being signed (true) or cancelled (false)
 * @param name the conquest name chosen by the player; only present on the first signing when
 *             the guild generated automatically and no name has been chosen yet
 */
public record SignContractPacket(boolean signed, Optional<String> name) implements CustomPacketPayload {

    public SignContractPacket(boolean signed) {
        this(signed, Optional.empty());
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SignContractPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SignContractPacket::signed,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), SignContractPacket::name,
            SignContractPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.SIGN_CONTRACT;
    }
}
