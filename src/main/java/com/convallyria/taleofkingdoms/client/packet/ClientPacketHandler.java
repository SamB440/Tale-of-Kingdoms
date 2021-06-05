package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
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
        ClientSidePacketRegistry.INSTANCE.register(this.getPacket(),
                (packetContext, attachedData) -> handleIncomingPacket(this.getPacket(), packetContext, attachedData));
    }

    protected void sendPacket(ClientConnection connection, PacketByteBuf passedData) {
        if (connection == null) MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(identifier, passedData));
        else connection.send(new CustomPayloadC2SPacket(identifier, passedData));
    }
}
