package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanBuyWorkerPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public final class IncomingForemanBuyWorkerPacketHandler extends InServerPacketHandler<ForemanBuyWorkerPacket> {

    public IncomingForemanBuyWorkerPacketHandler() {
        super(Packets.FOREMAN_BUY_WORKER, ForemanBuyWorkerPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, ForemanBuyWorkerPacket packet) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        int entityId = packet.entityId();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final Entity entityById = player.getWorld().getEntityById(entityId);
            if (!(entityById instanceof ForemanEntity foremanEntity) || entityById.distanceTo(player) > 5) {
                reject(player, "Invalid entity ID / Distance");
                return;
            }

            foremanEntity.buyWorker(player, instance);
        }));
    }
}
