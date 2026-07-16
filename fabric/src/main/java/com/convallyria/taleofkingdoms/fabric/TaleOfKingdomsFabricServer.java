package com.convallyria.taleofkingdoms.fabric;

import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import net.fabricmc.api.DedicatedServerModInitializer;

/**
 * Fabric dedicated-server entrypoint. Delegates to the loader-agnostic
 * {@link TaleOfKingdomsServer#init()} (only registered server-side).
 */
public final class TaleOfKingdomsFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        new TaleOfKingdomsServer().init();
    }
}
