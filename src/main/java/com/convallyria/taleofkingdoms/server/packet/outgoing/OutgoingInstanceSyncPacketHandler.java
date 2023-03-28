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

import java.util.UUID;

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
            // Copy the instance but only make it reference the current player. We don't want to leak data (or waste bandwidth).
            ConquestInstance copy = new ConquestInstance(instance.getName(), instance.getStart(), instance.getEnd(), instance.getOrigin());
            copy.uploadData(instance);
            for (UUID uuid : copy.getGuildPlayers().keySet()) {
                if (uuid.equals(player.getUuid())) continue;
                copy.getGuildPlayers().remove(uuid);
            }
            passedData.encodeAsJson(ConquestInstance.CODEC, copy);
            sendPacket(player, passedData);
        }
    }
}
