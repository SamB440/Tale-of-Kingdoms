package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingSignContractPacketHandler extends ServerPacketHandler {

    public IncomingSignContractPacketHandler() {
        super(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        boolean sign = attachedData.readBoolean();
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                    ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                    PlayerEntity playerEntity = context.getPlayer();
                    //TODO verification server-side
                    serverConquestInstance.setHasContract(playerEntity.getUuid(), sign);
                    this.handleOutgoingPacket(identifier, playerEntity, null, true);
                });
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        if (data != null && data[0] instanceof Boolean) {
            boolean sign = (Boolean) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(sign);
            // Then we'll send the packet to all the players
            if (connection != null) connection.send(new CustomPayloadS2CPacket(identifier, passedData));
            else ((ServerPlayerEntity) player).networkHandler.connection.send(new CustomPayloadS2CPacket(identifier, passedData));
        }
    }
}
