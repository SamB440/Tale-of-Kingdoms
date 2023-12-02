package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
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

    public void registerClientHandler(ClientPacketHandler clientPacketHandler) {
        clientPacketHandlers.put(clientPacketHandler.getPacket(), clientPacketHandler);
    }
}
