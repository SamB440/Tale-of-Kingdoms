package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ClientPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public abstract class ClientPacketHandler extends PacketHandler {

    public ClientPacketHandler(ResourceLocation packet) {
        super(packet);
    }

    @Override
    protected void register() {
        ClientPlayNetworking.registerGlobalReceiver(getPacket(), (client, handler, buf, responseSender) -> {
            ClientPacketContext context = new ClientPacketContext(EnvType.CLIENT, client.player, client);
            handleIncomingPacket(getPacket(), context, buf);
        });
    }

    @Override
    protected void sendPacket(Player player, FriendlyByteBuf passedData) {
        ClientPlayNetworking.send(getPacket(), passedData);
    }
}
