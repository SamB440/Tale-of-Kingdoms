package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.dedicated.DedicatedServer;

public interface GameInstanceCallback {

    Event<GameInstanceCallback> EVENT = EventFactory.createArrayBacked(GameInstanceCallback.class,
            (listeners) -> gameInstance -> {
                for (GameInstanceCallback listener : listeners) {
                    listener.setGameInstance(gameInstance);
                }
            });

    void setGameInstance(DedicatedServer gameInstance);
}
