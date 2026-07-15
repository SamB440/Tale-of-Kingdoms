package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.UpgradeKingdomPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

public final class IncomingUpgradeKingdomPacketHandler extends InServerPacketHandler<UpgradeKingdomPacket> {

    public IncomingUpgradeKingdomPacketHandler() {
        super(Packets.UPGRADE_KINGDOM, UpgradeKingdomPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, UpgradeKingdomPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        UUID uuid = player.getUUID();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            throw new UnsupportedOperationException();
        }));
    }
}
