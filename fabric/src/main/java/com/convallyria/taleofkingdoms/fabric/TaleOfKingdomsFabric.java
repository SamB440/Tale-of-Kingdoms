package com.convallyria.taleofkingdoms.fabric;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric main entrypoint. Delegates to the loader-agnostic {@link TaleOfKingdoms#init()}.
 */
public final class TaleOfKingdomsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new TaleOfKingdoms().init();
    }
}
