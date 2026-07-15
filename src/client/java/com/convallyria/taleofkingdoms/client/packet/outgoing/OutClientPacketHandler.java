package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public abstract class OutClientPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public OutClientPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        PayloadTypeRegistry.playC2S().register(this.getPacket(), codec);
    }

    @Override
    public void sendPacket(Player player, T packet) {
        ClientPlayNetworking.send(packet);
    }
}
