package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingInstanceSyncPacketHandler extends ClientPacketHandler {

    public IncomingInstanceSyncPacketHandler() {
        super(Packets.INSTANCE_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        final ConquestInstance instance = attachedData.decodeAsJson(ConquestInstance.CODEC);
        context.taskQueue().execute(() -> {
            final PlayerEntity player = context.player();
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.getConquestInstanceStorage().addConquest(player.getUuidAsString(), instance, true);
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalStateException("Not supported");
    }
}
