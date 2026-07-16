package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface GameJoinCallback {

    Event<GameJoinCallback> EVENT = EventFactory.of(
            (listeners) -> () -> {
                for (GameJoinCallback listener : listeners) {
                    listener.update();
                }
            });

    void update();
}
