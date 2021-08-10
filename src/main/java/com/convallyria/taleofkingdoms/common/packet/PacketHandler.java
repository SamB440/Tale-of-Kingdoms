package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TaleOfKingdoms.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private final ResourceLocation packet;

    /**
     * Creates a new packet handler and registers it for incoming handling.
     * @param packet the {@link ResourceLocation} for the packet
     */
    public PacketHandler(ResourceLocation packet) {
        TaleOfKingdoms.LOGGER.info("Registered packet handler [" + this.getClass().getSimpleName() + "]");
        this.packet = packet;
        register();
    }

    /**
     * Gets the packet identifier.
     * @return the {@link ResourceLocation}
     */
    public ResourceLocation getPacket() {
        return packet;
    }

    /**
     * Handles a packet that is incoming (client receiving from server, or server receiving from client)
     * @param identifier packet {@link ResourceLocation}
     * @param context the {@link Player}
     * @param attachedData the {@link FriendlyByteBuf}, which is data sent via {@link #handleOutgoingPacket(ResourceLocation, Player, Connection, Object...)}
     */
    public abstract void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData);

    /**
     * Handles a packet that is outgoing (client sending to server, or server sending to client)
     * @param identifier packet {@link ResourceLocation}
     * @param player the player sending it
     * @param connection the player's connection, can be null
     * @param data extra data to post with the packet, some packets may require different data
     */
    public abstract void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player, @Nullable Connection connection, @Nullable Object... data);

    protected abstract void sendPacket(Player player, FriendlyByteBuf passedData);

    /**
     * Registers the packet to receive incoming data.
     */
    protected abstract void register();
}
