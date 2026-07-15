package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanCollectPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class IncomingForemanCollectPacketHandler extends InServerPacketHandler<ForemanCollectPacket> {

    public IncomingForemanCollectPacketHandler() {
        super(Packets.FOREMAN_COLLECT, ForemanCollectPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, ForemanCollectPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        int entityId = packet.entityId();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final Entity entityById = player.level().getEntity(entityId);
            if (!(entityById instanceof ForemanEntity foremanEntity) || entityById.distanceTo(player) > 5) {
                reject(player, "Invalid entity ID / Distance");
                return;
            }

            foremanEntity.collect64(player, entityById instanceof QuarryForemanEntity ? Items.COBBLESTONE : Items.OAK_LOG);
        }));
    }
}
