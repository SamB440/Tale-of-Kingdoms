package com.convallyria.taleofkingdoms.client.packet;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ClientPacketHandler extends PacketHandler {

    public ClientPacketHandler(ResourceLocation packet) {
        super(packet);
    }

    @Override
    protected void register() {
        /*ClientPlayNetworking.registerGlobalReceiver(getPacket(), (client, handler, buf, responseSender) -> {
            ClientPacketContext context = new ClientPacketContext(Dist.CLIENT, client.player, client);
            handleIncomingPacket(getPacket(), context, buf);
        });*/
    }

    @Override
    protected void sendPacket(Player player, FriendlyByteBuf passedData) {
        //ClientPlayNetworking.send(getPacket(), passedData);
    }
}
