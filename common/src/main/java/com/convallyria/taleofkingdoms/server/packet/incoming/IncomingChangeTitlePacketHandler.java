package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ChangeTitlePacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.server.level.ServerPlayer;

public final class IncomingChangeTitlePacketHandler extends InServerPacketHandler<ChangeTitlePacket> {

    public IncomingChangeTitlePacketHandler() {
        super(Packets.CHANGE_TITLE, ChangeTitlePacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, ChangeTitlePacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            if (guildPlayer == null) {
                TaleOfKingdoms.LOGGER.info("Rejected {}: Player not part of instance.", getPacket().toString());
                return;
            }

            TaleOfKingdoms.LOGGER.info("Handling title change for player {}: {}", player.getUUID(), packet.title());
            guildPlayer.setTitle(packet.title());
            ServerConquestInstance.sync(player, instance);
        }));
    }
}
