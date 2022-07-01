package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingSignContractPacketHandler extends ServerPacketHandler {

    public IncomingSignContractPacketHandler() {
        super(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        boolean sign = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                PlayerEntity playerEntity = context.player();
                Optional<GuildMasterEntity> entity = instance.getGuildEntity(playerEntity.world, EntityTypes.GUILDMASTER);
                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + ": GuildMaster entity returned null.");
                    return;
                }

                if (!playerEntity.getEyePos().isInRange(entity.get().getPos(), 8)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + ": Not in range.");
                    return;
                }

                TaleOfKingdoms.LOGGER.info("Handling contract sign for player " + playerEntity.getUuid() + ": " + sign);
                instance.setHasContract(playerEntity.getUuid(), sign);
                this.handleOutgoingPacket(identifier, playerEntity, true);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && data[0] instanceof Boolean) {
            boolean sign = (Boolean) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(sign);
            sendPacket(player, passedData);
        }
    }
}
