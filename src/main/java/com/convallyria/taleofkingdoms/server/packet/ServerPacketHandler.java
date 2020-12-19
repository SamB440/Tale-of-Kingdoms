package com.convallyria.taleofkingdoms.server.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public abstract class ServerPacketHandler extends PacketHandler {

    public ServerPacketHandler(Identifier packet) {
        super(packet);
    }

    @Override
    protected void register() {
        ServerSidePacketRegistry.INSTANCE.register(this.getPacket(),
            (packetContext, attachedData) -> handleIncomingPacket(this.getPacket(), packetContext, attachedData));
    }
}
