package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface WorldSessionStartCallback {

    Event<WorldSessionStartCallback> EVENT = EventFactory.of(
            (listeners) -> (worldName) -> {
                for (WorldSessionStartCallback listener : listeners) {
                    listener.start(worldName);
                }
            });

    void start(String worldName);

}
