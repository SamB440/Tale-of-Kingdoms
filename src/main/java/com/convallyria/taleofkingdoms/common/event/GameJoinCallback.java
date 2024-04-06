package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GameJoinCallback {

    Event<GameJoinCallback> EVENT = EventFactory.createArrayBacked(GameJoinCallback.class,
            (listeners) -> () -> {
                for (GameJoinCallback listener : listeners) {
                    listener.update();
                }
            });

    void update();
}
