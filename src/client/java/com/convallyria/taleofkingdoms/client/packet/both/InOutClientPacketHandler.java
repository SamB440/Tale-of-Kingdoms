package com.convallyria.taleofkingdoms.client.packet.both;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ClientPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

@Environment(EnvType.CLIENT)
public abstract class InOutClientPacketHandler<T extends CustomPayload> extends PacketHandler<T> {

    public InOutClientPacketHandler(CustomPayload.Id<T> packet, PacketCodec<RegistryByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        PayloadTypeRegistry.playS2C().register(this.getPacket(), codec);
        PayloadTypeRegistry.playC2S().register(this.getPacket(), codec);
        ClientPlayNetworking.registerGlobalReceiver(getPacket(), (payload, ctx) -> {
            ClientPacketContext context = new ClientPacketContext(EnvType.CLIENT, ctx.player(), MinecraftClient.getInstance());
            handleIncomingPacket(context, payload);
        });
    }

    @Override
    public void sendPacket(PlayerEntity player, T packet) {
        ClientPlayNetworking.send(packet);
    }
}
