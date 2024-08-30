package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClientAPI extends TaleOfKingdomsAPI {

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
}
