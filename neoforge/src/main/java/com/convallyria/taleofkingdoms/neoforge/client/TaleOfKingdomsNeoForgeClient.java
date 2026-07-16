package com.convallyria.taleofkingdoms.neoforge.client;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;

/**
 * Client-only bootstrap, referenced from {@link com.convallyria.taleofkingdoms.neoforge.TaleOfKingdomsNeoForge}
 * via a method reference so the client class graph is never loaded on a dedicated server.
 */
public final class TaleOfKingdomsNeoForgeClient {

    private TaleOfKingdomsNeoForgeClient() {
    }

    public static void init() {
        new TaleOfKingdomsClient().init();
    }
}
