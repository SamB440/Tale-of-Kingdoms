package com.convallyria.taleofkingdoms.server.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.SERVER)
public class ServerConquestInstance {

    public static void sync(@NotNull ServerPlayerEntity player, @NotNull ConquestInstance instance) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        PacketHandler packetHandler = api.getServerPacketHandler(Packets.INSTANCE_SYNC);
        packetHandler.handleOutgoingPacket(player, instance);
    }
}
