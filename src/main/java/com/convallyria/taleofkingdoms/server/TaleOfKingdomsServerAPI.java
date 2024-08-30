package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.util.function.Consumer;

@Environment(EnvType.SERVER)
public class TaleOfKingdomsServerAPI extends TaleOfKingdomsAPI {

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
}
