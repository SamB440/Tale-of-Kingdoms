package com.convallyria.taleofkingdoms.server.world;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServerAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.SERVER)
public class ServerConquestInstance {

    public static void sync(@NotNull ServerPlayerEntity player, @NotNull ConquestInstance instance) {
        final TaleOfKingdomsServerAPI api = TaleOfKingdomsServer.getAPI();
        PacketHandler packetHandler = api.getServerPacketHandler(Packets.INSTANCE_SYNC);
        packetHandler.handleOutgoingPacket(player, instance);
    }
}
