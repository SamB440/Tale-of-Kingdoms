package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingSignContractPacketHandler extends ServerPacketHandler {

    public IncomingSignContractPacketHandler() {
        super(Packets.SIGN_CONTRACT);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        boolean sign = attachedData.readBoolean();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            PlayerEntity player = context.player();
            Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.world, EntityTypes.GUILDMASTER);
            if (entity.isEmpty()) {
                TaleOfKingdoms.LOGGER.info("Rejected " + getPacket().toString() + ": GuildMaster entity returned null.");
                return;
            }

            if (!player.getEyePos().isInRange(entity.get().getPos(), 8)) {
                TaleOfKingdoms.LOGGER.info("Rejected " + getPacket().toString() + ": Not in range.");
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player);
            TaleOfKingdoms.LOGGER.info("Handling contract sign for player " + player.getUuid() + ": " + sign);
            guildPlayer.setSignedContract(sign);
            this.handleOutgoingPacket(player, true);
        }));
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && data[0] instanceof Boolean) {
            boolean sign = (Boolean) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(sign);
            sendPacket(player, passedData);
        }
    }
}
