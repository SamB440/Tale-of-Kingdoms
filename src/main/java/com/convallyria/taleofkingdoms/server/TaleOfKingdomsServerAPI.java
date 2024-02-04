package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Environment(EnvType.SERVER)
public class TaleOfKingdomsServerAPI extends TaleOfKingdomsAPI {

    private final Map<Identifier, ServerPacketHandler> serverPacketHandlers = new ConcurrentHashMap<>();
    private MinecraftDedicatedServer minecraftServer;

    public TaleOfKingdomsServerAPI(TaleOfKingdoms mod) {
        super(mod);
    }

    @Override
    public void executeOnServerEnvironment(Consumer<MinecraftServer> runnable) {
        executeOnDedicatedServer(() -> runnable.accept(minecraftServer));
    }

    @Override
    public void executeOnMain(Runnable runnable) {
        executeOnDedicatedServer(runnable);
    }

    @Override
    public PacketHandler getPacketHandler(Identifier packet) {
        return getServerPacketHandler(packet);
    }

    /**
     * Executes a task on the dedicated server.
     * @param runnable task to run
     */
    public void executeOnDedicatedServer(Runnable runnable) {
        minecraftServer.execute(runnable);
    }

    public MinecraftDedicatedServer getServer() {
        return minecraftServer;
    }

    public void setServer(MinecraftDedicatedServer minecraftServer) {
        if (this.minecraftServer != null) {
            throw new IllegalStateException("Server already registered");
        }

        this.minecraftServer = minecraftServer;
    }

    public ServerPacketHandler getServerPacketHandler(Identifier identifier) {
        return serverPacketHandlers.get(identifier);
    }

    public void registerServerHandler(ServerPacketHandler serverPacketHandler) {
        serverPacketHandlers.put(serverPacketHandler.getPacket(), serverPacketHandler);
    }
}
