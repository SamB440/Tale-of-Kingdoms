package net.islandearth.taleofkingdoms.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.islandearth.taleofkingdoms.common.listener.GameInstanceListener;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        this.registerListeners();
    }

    private void registerListeners() {
        new GameInstanceListener();
    }
}
