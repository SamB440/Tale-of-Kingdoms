package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.scheduler.Scheduler;
import com.convallyria.taleofkingdoms.common.schematic.ClientSchematicHandler;
import com.convallyria.taleofkingdoms.common.schematic.SchematicHandler;
import com.convallyria.taleofkingdoms.common.schematic.ServerSchematicHandler;
import com.convallyria.taleofkingdoms.common.world.ConquestInstanceStorage;
import com.convallyria.taleofkingdoms.managers.IManager;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TaleOfKingdomsAPI {

    private final TaleOfKingdoms mod;
    private final ConquestInstanceStorage cis;
    private final Map<String, IManager> managers = new HashMap<>();
    private MinecraftDedicatedServer minecraftServer;
    @Environment(EnvType.SERVER)
    private Map<Identifier, ServerPacketHandler> serverPacketHandlers = new ConcurrentHashMap<>();
    @Environment(EnvType.CLIENT)
    private Map<Identifier, ClientPacketHandler> clientPacketHandlers = new ConcurrentHashMap<>();
    private final Scheduler scheduler;

    public TaleOfKingdomsAPI(TaleOfKingdoms mod) {
        this.mod = mod;
        this.cis = new ConquestInstanceStorage();
        SoundManager sm = new SoundManager(mod);
        managers.put(sm.getName(), sm);
        this.scheduler = new Scheduler();
    }

    @Environment(EnvType.SERVER)
    public ServerPacketHandler getServerHandler(Identifier identifier) {
        return serverPacketHandlers.get(identifier);
    }

    @Environment(EnvType.SERVER)
    public void registerServerHandler(ServerPacketHandler serverPacketHandler) {
        serverPacketHandlers.put(serverPacketHandler.getPacket(), serverPacketHandler);
    }

    @Environment(EnvType.CLIENT)
    public ClientPacketHandler getClientHandler(Identifier identifier) {
        return clientPacketHandlers.get(identifier);
    }

    @Environment(EnvType.CLIENT)
    public void registerClientHandler(ClientPacketHandler clientPacketHandler) {
        clientPacketHandlers.put(clientPacketHandler.getPacket(), clientPacketHandler);
    }

    @NotNull
    public Scheduler getScheduler() {
        return scheduler;
    }

    @NotNull
    public ConquestInstanceStorage getConquestInstanceStorage() {
        return cis;
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return mod.getDataFolder();
    }

    @NotNull
    public TaleOfKingdoms getMod() {
        return mod;
    }

    /**
     * Gets a manager by its name.
     * @param name name of the {@link IManager}
     * @return the {@link IManager} or null if not found
     */
    @Nullable
    public IManager getManager(String name) {
        return managers.get(name);
    }

    @NotNull
    public Set<String> getManagers() {
        return managers.keySet();
    }

    @Environment(EnvType.CLIENT)
    public void executeOnMain(Runnable runnable) {
        MinecraftClient.getInstance().execute(runnable);
    }

    @Environment(EnvType.CLIENT)
    public void executeOnServer(Runnable runnable) {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        if (server != null) {
            MinecraftClient.getInstance().getServer().execute(runnable);
        } else {
            TaleOfKingdoms.LOGGER.warn("Cannot execute task because MinecraftServer is null");
        }
    }

    /**
     * Executes a task on the dedicated server.
     * @param runnable task to run
     * @return true if {@link MinecraftDedicatedServer} was present, false if not
     */
    @Environment(EnvType.SERVER)
    public boolean executeOnDedicatedServer(Runnable runnable) {
        if (minecraftServer != null) {
            minecraftServer.execute(runnable);
            return true;
        }
        return false;
    }

    @Environment(EnvType.SERVER)
    public Optional<MinecraftDedicatedServer> getServer() {
        return Optional.ofNullable(minecraftServer);
    }

    @Environment(EnvType.SERVER)
    public void setServer(MinecraftDedicatedServer minecraftServer) {
        if (this.minecraftServer != null) {
            throw new IllegalStateException("Server already registered");
        }

        this.minecraftServer = minecraftServer;
    }

    public SchematicHandler getSchematicHandler() {
        if (this.minecraftServer != null) {
            return new ServerSchematicHandler();
        } else {
            return new ClientSchematicHandler();
        }
    }
}
