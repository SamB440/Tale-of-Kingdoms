package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface WorldStopCallback {

    Event<WorldStopCallback> EVENT = EventFactory.of(
            (listeners) -> () -> {
                for (WorldStopCallback listener : listeners) {
                    listener.stop();
                }
            });

    void stop();
}
