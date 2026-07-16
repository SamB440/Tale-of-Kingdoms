package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.packet.s2c.SignContractResponsePacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.world.entity.player.Player;

public final class IncomingSignContractResponsePacketHandler extends InClientPacketHandler<SignContractResponsePacket> {

    public IncomingSignContractResponsePacketHandler() {
        super(Packets.SIGN_CONTRACT_RESPONSE, SignContractResponsePacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, SignContractResponsePacket packet) {
        boolean sign = packet.signed();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final Player player = context.player();
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            guildPlayer.setSignedContract(sign);
            if (sign) Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
        }));
    }
}
