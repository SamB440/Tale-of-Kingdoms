package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class PacketHandler<T extends CustomPayload> {

    protected final CustomPayload.Id<T> packet;
    protected final PacketCodec<RegistryByteBuf, T> codec;

    /**
     * Creates a new packet handler and registers it for incoming handling.
     * @param packet the {@link Identifier} for the packet
     */
    public PacketHandler(CustomPayload.Id<T> packet, PacketCodec<RegistryByteBuf, T> codec) {
        TaleOfKingdoms.LOGGER.info("Registered packet handler [" + this.getClass().getSimpleName() + "]");
        this.packet = packet;
        this.codec = codec;
        register();
    }

    /**
     * Gets the packet identifier.
     * @return the {@link CustomPayload.Id<T>}
     */
    public CustomPayload.Id<T> getPacket() {
        return packet;
    }

    /**
     * Handles a packet that is incoming (client receiving from server, or server receiving from client)
     * @param context the {@link PlayerEntity}
     * @param packet the {@link CustomPayload}
     */
    public void handleIncomingPacket(PacketContext context, T packet) {
        throw new UnsupportedOperationException("Not supported");
    }

    public abstract void sendPacket(PlayerEntity player, T packet);

    /**
     * Registers the packet to receive incoming data.
     */
    protected abstract void register();

    public void reject(ServerPlayerEntity player, String log) {
        String playerContext = this.packet.toString() + " @ <" + player.getName().getString() + ":" + player.getIp() + ">";
        TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": " + log);
    }
}
