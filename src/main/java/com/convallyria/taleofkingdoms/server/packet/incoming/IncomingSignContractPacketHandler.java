package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
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
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                PlayerEntity playerEntity = context.player();
                //TODO verification server-side
                serverConquestInstance.setHasContract(playerEntity.getUuid(), sign);
                this.handleOutgoingPacket(identifier, playerEntity, null, true);
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
            sendPacket(player,passedData);
        }
    }
}
