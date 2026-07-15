package com.convallyria.taleofkingdoms.client.packet.both;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ClientPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public abstract class InOutClientPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public InOutClientPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        PayloadTypeRegistry.playS2C().register(this.getPacket(), codec);
        PayloadTypeRegistry.playC2S().register(this.getPacket(), codec);
        ClientPlayNetworking.registerGlobalReceiver(getPacket(), (payload, ctx) -> {
            ClientPacketContext context = new ClientPacketContext(EnvType.CLIENT, ctx.player(), Minecraft.getInstance());
            handleIncomingPacket(context, payload);
        });
    }

    @Override
    public void sendPacket(Player player, T packet) {
        ClientPlayNetworking.send(packet);
    }
}
