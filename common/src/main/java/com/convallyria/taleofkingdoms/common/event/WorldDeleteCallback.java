package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface WorldDeleteCallback {

    Event<WorldDeleteCallback> EVENT = EventFactory.of(
            (listeners) -> (name) -> {
                for (WorldDeleteCallback listener : listeners) {
                    listener.delete(name);
                }
            });

    void delete(String name);
}
