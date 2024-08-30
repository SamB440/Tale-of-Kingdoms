package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

@Environment(EnvType.CLIENT)
public abstract class OutClientPacketHandler<T extends CustomPayload> extends PacketHandler<T> {

    public OutClientPacketHandler(CustomPayload.Id<T> packet, PacketCodec<RegistryByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        PayloadTypeRegistry.playC2S().register(this.getPacket(), codec);
    }

    @Override
    public void sendPacket(PlayerEntity player, T packet) {
        ClientPlayNetworking.send(packet);
    }
}
