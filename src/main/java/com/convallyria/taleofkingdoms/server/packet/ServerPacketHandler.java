package com.convallyria.taleofkingdoms.server.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class ServerPacketHandler extends PacketHandler {

    public ServerPacketHandler(Identifier packet) {
        super(packet);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalStateException("Not supported yet");
    }

    @Override
    protected void register() {
        // Not supported yet.
        return;
    }
}
