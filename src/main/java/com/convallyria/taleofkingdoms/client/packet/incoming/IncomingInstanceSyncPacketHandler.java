package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                if (api.getConquestInstanceStorage().getConquestInstance(world).isPresent()) {
                    ClientConquestInstance instance = (ClientConquestInstance) api.getConquestInstanceStorage().getConquestInstance(world).get();
                    instance.setBankerCoins(bankerCoins);
                    instance.setCoins(coins);
                    instance.setWorthiness(worthiness);
                    instance.setFarmerLastBread(farmerLastBread);
                    instance.setHasContract(hasContract);
                    instance.setLoaded(isLoaded);
                    return;
                }

                ClientConquestInstance instance = new ClientConquestInstance(world, name, start, end, origin);
                instance.setBankerCoins(bankerCoins);
                instance.setCoins(coins);
                instance.setWorthiness(worthiness);
                instance.setFarmerLastBread(farmerLastBread);
                instance.setHasContract(hasContract);
                instance.setLoaded(isLoaded);
                api.getConquestInstanceStorage().addConquest(world, instance, true);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable ClientConnection connection, @Nullable Object... data) {
        throw new IllegalStateException("Not supported");
    }
}
