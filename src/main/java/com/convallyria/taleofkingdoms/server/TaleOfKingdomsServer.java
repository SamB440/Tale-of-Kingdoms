package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        this.registerListeners();
    }

    private void registerListeners() {
        new GameInstanceListener();
    }
}
