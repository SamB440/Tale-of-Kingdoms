package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class OutServerPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public OutServerPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        // The client registers open_client_screen both as an incoming handler and as this outgoing
        // handler for the integrated server; the incoming registration already supplied the codec.
        // Registering the type twice is fatal on NeoForge (deferred to RegisterPayloadHandlersEvent,
        // so it cannot be caught here), hence the explicit tracking instead of try/catch.
        if (markS2CTypeRegistered(getPacket())) {
            NetworkManager.registerS2CPayloadType(getPacket(), codec);
        } else {
            TaleOfKingdoms.LOGGER.info("Skipping duplicate network registration for packet: {}", this.getPacket().id().toString());
        }
    }

    @Override
    public void sendPacket(Player player, T packet) {
        NetworkManager.sendToPlayer((ServerPlayer) player, packet);
    }
}
