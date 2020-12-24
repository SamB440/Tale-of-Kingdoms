package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PacketHandler {

    private final Identifier packet;

    /**
     * Creates a new packet handler and registers it for incoming handling.
     * @param packet the {@link Identifier} for the packet
     */
    public PacketHandler(Identifier packet) {
        TaleOfKingdoms.LOGGER.info("Registered packet handler [" + this.getClass().getSimpleName() + "]");
        this.packet = packet;
        register();
    }

    /**
     * Gets the packet identifier.
     * @return the {@link Identifier}
     */
    public Identifier getPacket() {
        return packet;
    }

    /**
     * Handles a packet that is incoming (client receiving from server, or server receiving from client)
     * @param identifier packet {@link Identifier}
     * @param context the {@link PacketContext}
     * @param attachedData the {@link PacketByteBuf}, which is data sent via {@link #handleOutgoingPacket(Identifier, PlayerEntity, ClientConnection, Object...)}
     */
    public abstract void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData);

    /**
     * Handles a packet that is outgoing (client sending to server, or server sending to client)
     * @param identifier packet {@link Identifier}
     * @param player the player sending it
     * @param connection the player's connection, can be null
     * @param data extra data to post with the packet, some packets may require different data
     */
    public abstract void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable ClientConnection connection, @Nullable Object... data);

    /**
     * Registers the packet to receive incoming data.
     */
    protected abstract void register();
}
