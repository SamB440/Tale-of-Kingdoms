package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpgradeKingdomPacket(int entityId) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, UpgradeKingdomPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpgradeKingdomPacket::entityId,
            UpgradeKingdomPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.UPGRADE_KINGDOM;
    }
}
