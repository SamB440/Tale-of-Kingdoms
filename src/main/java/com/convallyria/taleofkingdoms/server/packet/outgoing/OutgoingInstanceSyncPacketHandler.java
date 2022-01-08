package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingInstanceSyncPacketHandler extends ServerPacketHandler {

    public OutgoingInstanceSyncPacketHandler() {
        super(TaleOfKingdoms.INSTANCE_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && data[0] instanceof ServerConquestInstance instance
                && player instanceof ServerPlayerEntity) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeString(instance.getName());
            passedData.writeString(instance.getWorld());
            passedData.writeInt(instance.getBankerCoins(player.getUuid()));
            passedData.writeInt(instance.getCoins(player.getUuid()));
            passedData.writeInt(instance.getWorthiness(player.getUuid()));
            passedData.writeLong(instance.getFarmerLastBread(player.getUuid()));
            passedData.writeBoolean(instance.hasContract(player.getUuid()));
            passedData.writeBoolean(instance.isLoaded());
            passedData.writeBlockPos(instance.getStart());
            passedData.writeBlockPos(instance.getEnd());
            passedData.writeBlockPos(instance.getOrigin());
            instance.getHunterUUIDs().forEach((playerUuid, hunterUuids) -> {
                hunterUuids.forEach(passedData::writeUuid);
            });
            sendPacket(player, passedData);
        }
    }
}
