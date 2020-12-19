package com.convallyria.taleofkingdoms.client.packet.both;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BothSignContractPacketHandler extends ClientPacketHandler {

    public BothSignContractPacketHandler() {
        super(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        boolean sign = attachedData.readBoolean();
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                    //TODO verify
                    TaleOfKingdoms.LOGGER.info("Server accepted? " + sign);
                });
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable ClientConnection connection, @Nullable Object... data) {
        if (data != null && data[0] instanceof Boolean) {
            boolean sign = (Boolean) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(sign);
            // Then we'll send the packet to all the players
            TaleOfKingdoms.LOGGER.info("SENDING PACKET");
            System.out.println(player + " " + identifier + " " + passedData);
            if (connection == null) MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(identifier, passedData));
            else connection.send(new CustomPayloadC2SPacket(identifier, passedData));
        }
    }
}
