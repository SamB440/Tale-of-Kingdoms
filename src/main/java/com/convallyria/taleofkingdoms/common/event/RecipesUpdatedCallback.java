package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RecipesUpdatedCallback {

    Event<RecipesUpdatedCallback> EVENT = EventFactory.createArrayBacked(RecipesUpdatedCallback.class,
            (listeners) -> () -> {
                for (RecipesUpdatedCallback listener : listeners) {
                    listener.update();
                }
            });

    void update();
}
