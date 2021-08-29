package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class IncomingInstanceSyncPacketHandler extends ClientPacketHandler {

    public IncomingInstanceSyncPacketHandler() {
        super(TaleOfKingdoms.INSTANCE_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        String name = attachedData.readString();
        String world = attachedData.readString();
        int bankerCoins = attachedData.readInt();
        int coins = attachedData.readInt();
        int worthiness = attachedData.readInt();
        long farmerLastBread = attachedData.readLong();
        boolean hasContract = attachedData.readBoolean();
        boolean isLoaded = attachedData.readBoolean();
        BlockPos start = attachedData.readBlockPos();
        BlockPos end = attachedData.readBlockPos();
        BlockPos origin = attachedData.readBlockPos();
        List<UUID> hunterUuids = new ArrayList<>();
        while (attachedData.isReadable()) {
            TaleOfKingdoms.LOGGER.info("Reading uuids!");
            hunterUuids.add(attachedData.readUuid());
        }

        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().ifPresent(api -> {
            ClientConquestInstance instance;
            if (api.getConquestInstanceStorage().getConquestInstance(world).isPresent()) {
                instance = (ClientConquestInstance) api.getConquestInstanceStorage().getConquestInstance(world).get();
            } else {
                instance = new ClientConquestInstance(world, name, start, end, origin);
            }

            instance.setBankerCoins(bankerCoins);
            instance.setCoins(coins);
            instance.setWorthiness(worthiness);
            instance.setFarmerLastBread(farmerLastBread);
            instance.setHasContract(hasContract);
            instance.setLoaded(isLoaded);
            instance.clearHunters();
            hunterUuids.forEach(instance::addHunter);
            api.getConquestInstanceStorage().addConquest(world, instance, true);
        }));
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalStateException("Not supported");
    }
}
