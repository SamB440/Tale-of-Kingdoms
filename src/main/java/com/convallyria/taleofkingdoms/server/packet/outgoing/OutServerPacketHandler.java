package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class OutServerPacketHandler<T extends CustomPayload> extends PacketHandler<T> {

    public OutServerPacketHandler(CustomPayload.Id<T> packet, PacketCodec<RegistryByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        try {
            PayloadTypeRegistry.playS2C().register(this.getPacket(), codec);
        } catch (IllegalArgumentException ignored) {
            // Terrible way to fix this problem but I can't think of a better way to set up the mod
            // We are registering an integrated server packet open_client_screen
            // So we have it registered as an incoming packet handler from server
            // And an outgoing via integrated server
            TaleOfKingdoms.LOGGER.warn("Ignoring duplicate registered packet: {}", this.getPacket().id().toString());
        }
    }

    @Override
    public void sendPacket(PlayerEntity player, T packet) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, packet);
    }
}
