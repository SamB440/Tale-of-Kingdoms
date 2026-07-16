package com.convallyria.taleofkingdoms.common.event.tok;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;

public interface GameInstanceLoadCallback {

    Event<GameInstanceLoadCallback> EVENT = EventFactory.of(
            (listeners) -> (instance) -> {
                for (GameInstanceLoadCallback listener : listeners) {
                    listener.load(instance);
                }
            });

    void load(ConquestInstance instance);

}
