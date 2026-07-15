package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ServerPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public abstract class InServerPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public InServerPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        PayloadTypeRegistry.playC2S().register(this.getPacket(), codec);
        ServerPlayNetworking.registerGlobalReceiver(this.getPacket(), (payload, ctx) -> {
            ServerPacketContext context = new ServerPacketContext(EnvType.SERVER, ctx.player(), ctx.player().getServer());
            handleIncomingPacket(context, payload);
        });
    }

    @Override
    public void sendPacket(Player player, T packet) {
        throw new UnsupportedOperationException();
    }
}
