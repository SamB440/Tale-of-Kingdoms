package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public abstract class ClientPacketHandler extends PacketHandler {

    public ClientPacketHandler(Identifier packet) {
        super(packet);
    }

    @Override
    protected void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.getPacket(),
                (packetContext, attachedData) -> {
            System.out.println(packetContext.getPacketEnvironment());
            handleIncomingPacket(this.getPacket(), packetContext, attachedData);
        });
    }
}
