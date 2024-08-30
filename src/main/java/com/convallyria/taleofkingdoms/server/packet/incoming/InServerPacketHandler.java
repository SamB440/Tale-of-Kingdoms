package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ServerPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public abstract class InServerPacketHandler<T extends CustomPayload> extends PacketHandler<T> {

    public InServerPacketHandler(CustomPayload.Id<T> packet, PacketCodec<RegistryByteBuf, T> codec) {
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
    public void sendPacket(PlayerEntity player, T packet) {
        throw new UnsupportedOperationException();
    }
}
