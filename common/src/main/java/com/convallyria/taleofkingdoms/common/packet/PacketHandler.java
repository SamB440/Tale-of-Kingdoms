package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class PacketHandler<T extends CustomPacketPayload> {

    /**
     * S2C payload types whose codec has already been registered with the loader in this JVM.
     * The client registers open_client_screen twice: once as an incoming handler and once as an
     * outgoing handler for the integrated server. Registering the network type twice is fatal on
     * NeoForge (deferred to RegisterPayloadHandlersEvent, so it cannot be caught at the call site),
     * so registration is tracked here and skipped for duplicates instead.
     */
    private static final Set<CustomPacketPayload.Type<?>> REGISTERED_S2C_TYPES = new HashSet<>();

    protected final CustomPacketPayload.Type<T> packet;
    protected final StreamCodec<RegistryFriendlyByteBuf, T> codec;

    /**
     * Creates a new packet handler and registers it for incoming handling.
     * @param packet the {@link ResourceLocation} for the packet
     */
    public PacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        TaleOfKingdoms.LOGGER.info("Registered packet handler [" + this.getClass().getSimpleName() + "]");
        this.packet = packet;
        this.codec = codec;
        register();
    }

    /**
     * Gets the packet identifier.
     * @return the {@link CustomPayload.Id<T>}
     */
    public CustomPacketPayload.Type<T> getPacket() {
        return packet;
    }

    /**
     * Handles a packet that is incoming (client receiving from server, or server receiving from client)
     * @param context the {@link Player}
     * @param packet the {@link CustomPacketPayload}
     */
    public void handleIncomingPacket(PacketContext context, T packet) {
        throw new UnsupportedOperationException("Not supported");
    }

    public abstract void sendPacket(Player player, T packet);

    /**
     * Registers the packet to receive incoming data.
     */
    protected abstract void register();

    /**
     * Marks an S2C payload type's codec as registered with the loader in this JVM.
     * @return true if this call was the first registration, false if it was already registered
     */
    protected static boolean markS2CTypeRegistered(CustomPacketPayload.Type<?> type) {
        return REGISTERED_S2C_TYPES.add(type);
    }

    public void reject(ServerPlayer player, String log) {
        String playerContext = this.packet.toString() + " @ <" + player.getName().getString() + ":" + player.getIpAddress() + ">";
        TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": " + log);
    }
}
