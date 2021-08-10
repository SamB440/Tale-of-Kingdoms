package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OutgoingInstanceSyncPacketHandler extends ServerPacketHandler {

    public OutgoingInstanceSyncPacketHandler() {
        super(TaleOfKingdoms.INSTANCE_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player,
                                     @Nullable Connection connection, @Nullable Object... data) {
        if (data != null && data[0] instanceof ServerConquestInstance instance
                && player instanceof ServerPlayer serverPlayerEntity) {
            FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
            passedData.writeUtf(instance.getName());
            passedData.writeUtf(instance.getWorld());
            passedData.writeInt(instance.getBankerCoins(player.getUUID()));
            passedData.writeInt(instance.getCoins(player.getUUID()));
            passedData.writeInt(instance.getWorthiness(player.getUUID()));
            passedData.writeLong(instance.getFarmerLastBread(player.getUUID()));
            passedData.writeBoolean(instance.hasContract(player.getUUID()));
            passedData.writeBoolean(instance.isLoaded());
            passedData.writeBlockPos(instance.getStart());
            passedData.writeBlockPos(instance.getEnd());
            passedData.writeBlockPos(instance.getOrigin());
            instance.getHunterUUIDs().forEach((playerUuid, hunterUuids) -> {
                hunterUuids.forEach(passedData::writeUUID);
            });
            sendPacket(player,passedData);
        }
    }
}
