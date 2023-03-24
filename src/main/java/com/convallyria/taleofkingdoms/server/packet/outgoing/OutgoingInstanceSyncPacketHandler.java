package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingInstanceSyncPacketHandler extends ServerPacketHandler {

    public OutgoingInstanceSyncPacketHandler() {
        super(Packets.INSTANCE_SYNC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && data[0] instanceof ConquestInstance instance
                && player instanceof ServerPlayerEntity) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.encodeAsJson(ConquestInstance.CODEC, instance);
            sendPacket(player, passedData);
        }
    }
}
