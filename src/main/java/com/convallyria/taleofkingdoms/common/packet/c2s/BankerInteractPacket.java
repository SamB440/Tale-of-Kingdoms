package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BankerInteractPacket(BankerMethod method, int coins) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, BankerInteractPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(i -> BankerMethod.values()[i], BankerMethod::ordinal), BankerInteractPacket::method,
            ByteBufCodecs.INT, BankerInteractPacket::coins,
            BankerInteractPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.BANKER_INTERACT;
    }
}
