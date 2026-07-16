package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public abstract class OutClientPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public OutClientPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        // Architectury has no registerC2SPayloadType; registering a (dormant) C2S receiver is the
        // supported path to register the C2S codec on the sending client so sendToServer can encode.
        // The client never receives C2S packets, so this receiver is never invoked.
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, getPacket(), codec, (payload, ctx) -> {});
    }

    @Override
    public void sendPacket(Player player, T packet) {
        NetworkManager.sendToServer(packet);
    }
}
