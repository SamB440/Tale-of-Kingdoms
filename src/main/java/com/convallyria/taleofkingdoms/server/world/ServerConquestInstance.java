package com.convallyria.taleofkingdoms.server.world;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.InstanceSyncPacket;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServerAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Environment(EnvType.SERVER)
public class ServerConquestInstance {

    public static void sync(@NotNull ServerPlayerEntity player, @NotNull ConquestInstance instance) {
        final TaleOfKingdomsServerAPI api = TaleOfKingdomsServer.getAPI();
        // Copy the instance but only make it reference the current player. We don't want to leak data (or waste bandwidth).
        ConquestInstance copy = new ConquestInstance(instance.getName(), instance.getStart(), instance.getEnd(), instance.getOrigin());
        copy.uploadData(instance);
        for (UUID uuid : copy.getGuildPlayers().keySet()) {
            if (uuid.equals(player.getUuid())) continue;
            copy.getGuildPlayers().remove(uuid);
        }
        PacketHandler<InstanceSyncPacket> packetHandler = api.getServerPacket(Packets.INSTANCE_SYNC);
        packetHandler.sendPacket(player, new InstanceSyncPacket(copy));
    }
}
