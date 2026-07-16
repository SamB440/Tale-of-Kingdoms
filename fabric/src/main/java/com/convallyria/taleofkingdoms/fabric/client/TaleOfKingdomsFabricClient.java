package com.convallyria.taleofkingdoms.fabric.client;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Fabric client entrypoint. Delegates to the loader-agnostic {@link TaleOfKingdomsClient#init()}.
 */
public final class TaleOfKingdomsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new TaleOfKingdomsClient().init();
    }
}
