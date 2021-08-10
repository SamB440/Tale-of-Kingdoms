package com.convallyria.taleofkingdoms.server.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ServerPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.SERVER)
public abstract class ServerPacketHandler extends PacketHandler {

    public ServerPacketHandler(ResourceLocation packet) {
        super(packet);
    }

    @Override
    protected void register() {
        ServerPlayNetworking.registerGlobalReceiver(this.getPacket(), (server, player, handler, buf, responseSender) -> {
            ServerPacketContext context = new ServerPacketContext(EnvType.SERVER, player, server);
            handleIncomingPacket(this.getPacket(), context, buf);
        });
    }

    @Override
    protected void sendPacket(Player player, FriendlyByteBuf passedData) {
        ServerPlayNetworking.send((ServerPlayer) player, getPacket(), passedData);
    }
}
