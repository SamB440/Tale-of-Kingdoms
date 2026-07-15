package com.convallyria.taleofkingdoms.common.packet.s2c;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record InstanceSyncPacket(ConquestInstance instance) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, InstanceSyncPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ConquestInstance.CODEC), sync -> {
                if (sync.instance().getGuildPlayers().size() > 1) {
                    throw new IllegalStateException("Leaking extra guild player information!");
                }
                return sync.instance();
            },
            InstanceSyncPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.INSTANCE_SYNC;
    }
}
