package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public interface GameInstanceCallback {

    Event<GameInstanceCallback> EVENT = EventFactory.createArrayBacked(GameInstanceCallback.class,
            (listeners) -> gameInstance -> {
                for (GameInstanceCallback listener : listeners) {
                    listener.setGameInstance(gameInstance);
                }
            });

    void setGameInstance(MinecraftDedicatedServer gameInstance);
}
