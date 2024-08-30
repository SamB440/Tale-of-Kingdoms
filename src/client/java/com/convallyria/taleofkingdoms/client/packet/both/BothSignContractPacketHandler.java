package com.convallyria.taleofkingdoms.client.packet.both;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.both.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.player.PlayerEntity;

public final class BothSignContractPacketHandler extends InOutClientPacketHandler<SignContractPacket> {

    public BothSignContractPacketHandler() {
        super(Packets.SIGN_CONTRACT, SignContractPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, SignContractPacket packet) {
        boolean sign = packet.signed();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final PlayerEntity player = context.player();
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            guildPlayer.setSignedContract(sign);
            if (sign) Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
        }));
    }
}
