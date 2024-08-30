package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.UpgradeKingdomPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class IncomingUpgradeKingdomPacketHandler extends InServerPacketHandler<UpgradeKingdomPacket> {

    public IncomingUpgradeKingdomPacketHandler() {
        super(Packets.UPGRADE_KINGDOM, UpgradeKingdomPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, UpgradeKingdomPacket packet) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        UUID uuid = player.getUuid();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            throw new UnsupportedOperationException();
        }));
    }
}
