package com.convallyria.taleofkingdoms.common.packet.s2c;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record InstanceSyncPacket(ConquestInstance instance) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, InstanceSyncPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.codec(ConquestInstance.CODEC), sync -> {
                if (sync.instance().getGuildPlayers().size() > 1) {
                    throw new IllegalStateException("Leaking extra guild player information!");
                }
                return sync.instance();
            },
            InstanceSyncPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.INSTANCE_SYNC;
    }
}
