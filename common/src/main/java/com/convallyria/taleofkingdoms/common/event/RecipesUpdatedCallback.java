package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface RecipesUpdatedCallback {

    Event<RecipesUpdatedCallback> EVENT = EventFactory.of(
            (listeners) -> () -> {
                for (RecipesUpdatedCallback listener : listeners) {
                    listener.update();
                }
            });

    void update();
}
