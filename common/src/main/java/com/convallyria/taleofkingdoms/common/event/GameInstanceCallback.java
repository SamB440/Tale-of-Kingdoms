package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.dedicated.DedicatedServer;

public interface GameInstanceCallback {

    Event<GameInstanceCallback> EVENT = EventFactory.of(
            (listeners) -> gameInstance -> {
                for (GameInstanceCallback listener : listeners) {
                    listener.setGameInstance(gameInstance);
                }
            });

    void setGameInstance(DedicatedServer gameInstance);
}
