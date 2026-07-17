package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.advancement.TOKAdvancements;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.packet.s2c.SignContractResponsePacket;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
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
            boolean named = false;
            if (sign && instance.getName() == null && packet.name().isPresent()) {
                // First signing with an automatically generated guild: the player names the conquest.
                final String name = packet.name().get().trim();
                if (!name.isEmpty()) {
                    instance.setName(name.substring(0, Math.min(name.length(), 32)));
                    named = true;
                }
            }
            guildPlayer.setSignedContract(sign);
            if (sign && player instanceof ServerPlayer serverPlayer) {
                // The location trigger grants the root when entering an auto-generated guild; grant it
                // here too so a legacy manually-placed guild (which isn't a registered structure) still
                // completes the root for anyone who signs the contract at it.
                TOKAdvancements.award(serverPlayer, TOKAdvancements.ROOT);
                TOKAdvancements.award(serverPlayer, TOKAdvancements.SIGN_CONTRACT);
            }
            // Re-sync so the client's copy of the instance learns the chosen name.
            if (named) ServerConquestInstance.sync((ServerPlayer) player, instance);
            TaleOfKingdoms.getAPI().getServerPacket(Packets.SIGN_CONTRACT_RESPONSE)
                    .sendPacket(player, new SignContractResponsePacket(true));
        }));
    }
}
