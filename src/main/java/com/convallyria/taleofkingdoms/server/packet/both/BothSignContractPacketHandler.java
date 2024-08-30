package com.convallyria.taleofkingdoms.server.packet.both;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.both.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public final class BothSignContractPacketHandler extends InOutServerPacketHandler<SignContractPacket> {

    public BothSignContractPacketHandler() {
        super(Packets.SIGN_CONTRACT, SignContractPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, SignContractPacket packet) {
        final boolean sign = packet.signed();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            PlayerEntity player = context.player();
            Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.getWorld(), EntityTypes.GUILDMASTER);
            if (entity.isEmpty()) {
                TaleOfKingdoms.LOGGER.info("Rejected {}: GuildMaster entity returned null.", getPacket().toString());
                return;
            }

            if (!player.getEyePos().isInRange(entity.get().getPos(), 8)) {
                TaleOfKingdoms.LOGGER.info("Rejected {}: Not in range.", getPacket().toString());
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player);
            TaleOfKingdoms.LOGGER.info("Handling contract sign for player {}: {}", player.getUuid(), sign);
            guildPlayer.setSignedContract(sign);
            this.sendPacket(player, new SignContractPacket(true));
        }));
    }
}
