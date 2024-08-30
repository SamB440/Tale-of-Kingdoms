package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record BankerInteractPacket(BankerMethod method, int coins) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, BankerInteractPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.indexed(i -> BankerMethod.values()[i], BankerMethod::ordinal), BankerInteractPacket::method,
            PacketCodecs.INTEGER, BankerInteractPacket::coins,
            BankerInteractPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.BANKER_INTERACT;
    }
}
