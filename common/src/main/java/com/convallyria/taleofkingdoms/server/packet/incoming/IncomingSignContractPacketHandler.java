package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.packet.s2c.SignContractResponsePacket;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;

public final class IncomingSignContractPacketHandler extends InServerPacketHandler<SignContractPacket> {

    public IncomingSignContractPacketHandler() {
        super(Packets.SIGN_CONTRACT, SignContractPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, SignContractPacket packet) {
        final boolean sign = packet.signed();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            Player player = context.player();
            Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.level(), EntityTypes.GUILDMASTER.get());
            if (entity.isEmpty()) {
                TaleOfKingdoms.LOGGER.info("Rejected {}: GuildMaster entity returned null.", getPacket().toString());
                return;
            }

            if (!player.getEyePosition().closerThan(entity.get().position(), 8)) {
                TaleOfKingdoms.LOGGER.info("Rejected {}: Not in range.", getPacket().toString());
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player);
            TaleOfKingdoms.LOGGER.info("Handling contract sign for player {}: {}", player.getUUID(), sign);
            guildPlayer.setSignedContract(sign);
            TaleOfKingdoms.getAPI().getServerPacket(Packets.SIGN_CONTRACT_RESPONSE)
                    .sendPacket(player, new SignContractResponsePacket(true));
        }));
    }
}
