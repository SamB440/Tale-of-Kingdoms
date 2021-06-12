package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ClientPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class ClientPacketHandler extends PacketHandler {

    private final Identifier identifier;

    public ClientPacketHandler(Identifier packet) {
        super(packet);
        this.identifier = packet;
    }

    @Override
    protected void register() {
        ClientPlayNetworking.registerGlobalReceiver(identifier, (client, handler, buf, responseSender) -> {
            ClientPacketContext context = new ClientPacketContext(EnvType.CLIENT, client.player, client);
            handleIncomingPacket(identifier, context, buf);
        });
    }

    @Override
    protected void sendPacket(PlayerEntity player, PacketByteBuf passedData) {
        ClientPlayNetworking.send(identifier, passedData);
    }
}
