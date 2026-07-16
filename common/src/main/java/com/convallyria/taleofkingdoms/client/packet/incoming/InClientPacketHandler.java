package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.ClientPacketContext;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public abstract class InClientPacketHandler<T extends CustomPacketPayload> extends PacketHandler<T> {

    public InClientPacketHandler(CustomPacketPayload.Type<T> packet, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        super(packet, codec);
    }

    @Override
    protected void register() {
        // Registering an S2C receiver also registers the payload type's codec with the loader.
        markS2CTypeRegistered(getPacket());
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, getPacket(), codec, (payload, ctx) -> {
            ClientPacketContext context = new ClientPacketContext(EnvType.CLIENT, ctx.getPlayer(), Minecraft.getInstance());
            handleIncomingPacket(context, payload);
        });
    }

    @Override
    public void sendPacket(Player player, T packet) {
        throw new UnsupportedOperationException();
    }
}
