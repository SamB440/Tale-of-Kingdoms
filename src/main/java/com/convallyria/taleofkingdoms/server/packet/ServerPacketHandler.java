package com.convallyria.taleofkingdoms.server.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public abstract class ServerPacketHandler extends PacketHandler {

    public ServerPacketHandler(ResourceLocation packet) {
        super(packet);
    }

    @Override
    protected void register() {
        /*PacketHandler.INSTANCE.(this.getPacket(), (server, player, handler, buf, responseSender) -> {
            ServerPacketContext context = new ServerPacketContext(Dist.DEDICATED_SERVER, player, server);
            handleIncomingPacket(this.getPacket(), context, buf);
        });*/
    }

    @Override
    protected void sendPacket(Player player, FriendlyByteBuf passedData) {
        //ServerPlayNetworking.send((ServerPlayer) player, getPacket(), passedData);
    }
}
