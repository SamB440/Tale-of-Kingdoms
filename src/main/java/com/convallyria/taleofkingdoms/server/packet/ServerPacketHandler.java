package com.convallyria.taleofkingdoms.server.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ServerPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.SERVER)
public abstract class ServerPacketHandler extends PacketHandler {

    public ServerPacketHandler(Identifier packet) {
        super(packet);
    }

    @Override
    protected void register() {
        ServerPlayNetworking.registerGlobalReceiver(this.getPacket(), (server, player, handler, buf, responseSender) -> {
            ServerPacketContext context = new ServerPacketContext(EnvType.SERVER, player, server);
            handleIncomingPacket(context, buf);
        });
    }

    @Override
    protected void sendPacket(PlayerEntity player, PacketByteBuf passedData) {
        ServerPlayNetworking.send((ServerPlayerEntity) player, getPacket(), passedData);
    }
}
