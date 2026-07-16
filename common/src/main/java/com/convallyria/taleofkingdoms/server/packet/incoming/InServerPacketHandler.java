package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ServerPacketContext;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public abstract class InServerPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public InServerPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, getPacket(), codec, (payload, ctx) -> {
            ServerPacketContext context = new ServerPacketContext(EnvType.SERVER, ctx.getPlayer(), ctx.getPlayer().getServer());
            handleIncomingPacket(context, payload);
        });
    }

    @Override
    public void sendPacket(Player player, T packet) {
        throw new UnsupportedOperationException();
    }
}
