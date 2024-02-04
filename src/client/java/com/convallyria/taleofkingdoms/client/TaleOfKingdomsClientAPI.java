package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClientAPI extends TaleOfKingdomsAPI {

    private final Map<Identifier, ClientPacketHandler> clientPacketHandlers = new ConcurrentHashMap<>();
    private final Map<Identifier, ServerPacketHandler> integratedPacketHandlers = new ConcurrentHashMap<>();

    public TaleOfKingdomsClientAPI(TaleOfKingdoms mod) {
        super(mod);
    }

    @Override
    public void executeOnServerEnvironment(Consumer<MinecraftServer> runnable) {
        executeOnServer(() -> runnable.accept(MinecraftClient.getInstance().getServer()));
    }

    @Override
    public void executeOnMain(Runnable runnable) {
        MinecraftClient.getInstance().execute(runnable);
    }

    @Override
    public PacketHandler getPacketHandler(Identifier packet) {
        // Integrated takes precedence over client
        final ServerPacketHandler possible = getIntegratedPacketHandler(packet);
        if (possible == null) {
            return getClientPacketHandler(packet);
        }
        return possible;
    }

    public void executeOnServer(Runnable runnable) {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        if (server != null) {
            MinecraftClient.getInstance().getServer().execute(runnable);
        } else {
            TaleOfKingdoms.LOGGER.warn("Cannot execute task because MinecraftServer is null");
        }
    }

    public ClientPacketHandler getClientPacketHandler(Identifier identifier) {
        return clientPacketHandlers.get(identifier);
    }

    public ServerPacketHandler getIntegratedPacketHandler(Identifier identifier) {
        return integratedPacketHandlers.get(identifier);
    }

    public void registerClientHandler(ClientPacketHandler clientPacketHandler) {
        clientPacketHandlers.put(clientPacketHandler.getPacket(), clientPacketHandler);
    }

    // Ugh... Why are integrated servers so complex? Why can't we just unify dedicated servers to integrated ones so that everything runs as expected?
    public void registerIntegratedHandler(ServerPacketHandler serverPacketHandler) {
        integratedPacketHandlers.put(serverPacketHandler.getPacket(), serverPacketHandler);
    }
}
