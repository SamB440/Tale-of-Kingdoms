package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingInstanceSyncPacketHandler extends ClientPacketHandler {

    public IncomingInstanceSyncPacketHandler() {
        super(Packets.INSTANCE_SYNC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        final ConquestInstance instance = attachedData.decodeAsJson(ConquestInstance.CODEC);
        context.taskQueue().execute(() -> {
            final PlayerEntity player = context.player();
            if (TaleOfKingdoms.CONFIG.mainConfig.developerMode) player.sendMessage(Text.literal("Received sync, " + instance));
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            final ConquestInstance existing = api.getConquestInstanceStorage().getConquestInstance(player.getUuidAsString()).orElse(null);
            if (existing != null) {
                existing.uploadData(instance);
            } else {
                api.getConquestInstanceStorage().addConquest(player.getUuidAsString(), instance, true);
            }
        });
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalStateException("Not supported");
    }
}
