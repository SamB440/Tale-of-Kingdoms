package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ClientPacketHandler extends PacketHandler {

    public ClientPacketHandler(Identifier packet) {
        super(packet);
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull ServerPlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        throw new IllegalStateException("Not supported yet");
    }

    @Override
    protected void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.getPacket(),
                (packetContext, attachedData) -> handleIncomingPacket(this.getPacket(), packetContext, attachedData));
    }
}
