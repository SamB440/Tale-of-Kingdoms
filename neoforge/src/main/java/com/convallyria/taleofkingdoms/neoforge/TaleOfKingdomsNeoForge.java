package com.convallyria.taleofkingdoms.neoforge;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.neoforge.client.TaleOfKingdomsNeoForgeClient;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.fml.common.Mod;

/**
 * NeoForge entrypoint. The {@code @Mod} constructor runs during mod construction, which is the
 * correct window for Architectury's deferred registrations to hook NeoForge's registry events.
 * Client-only bootstrap is loaded via {@link EnvExecutor} so it never classloads on a server.
 */
@Mod(TaleOfKingdoms.MODID)
public final class TaleOfKingdomsNeoForge {

    public TaleOfKingdomsNeoForge() {
        new TaleOfKingdoms().init();
        EnvExecutor.runInEnv(Env.CLIENT, () -> TaleOfKingdomsNeoForgeClient::init);
        // Env.SERVER == dedicated server, matching Fabric's DedicatedServerModInitializer.
        EnvExecutor.runInEnv(Env.SERVER, () -> () -> new TaleOfKingdomsServer().init());
    }
}
