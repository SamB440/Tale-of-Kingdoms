package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PacketHandler {

    protected final Identifier packet;

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
     * @param context the {@link PlayerEntity}
     * @param attachedData the {@link PacketByteBuf}, which is data sent via {@link #handleOutgoingPacket(PlayerEntity, Object...)}
     */
    public abstract void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData);

    /**
     * Handles a packet that is outgoing (client sending to server, or server sending to client)
     * @param player the player sending it
     * @param data extra data to post with the packet, some packets may require different data
     */
    public abstract void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data);

    protected abstract void sendPacket(PlayerEntity player, PacketByteBuf passedData);

    /**
     * Registers the packet to receive incoming data.
     */
    protected abstract void register();

    public void reject(ServerPlayerEntity player, String log) {
        String playerContext = this.packet.toString() + " @ <" + player.getName().getString() + ":" + player.getIp() + ">";
        TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": " + log);
    }
}
