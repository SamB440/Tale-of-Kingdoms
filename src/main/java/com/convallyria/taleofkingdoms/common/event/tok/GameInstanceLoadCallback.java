package com.convallyria.taleofkingdoms.common.event.tok;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface GameInstanceLoadCallback {

    Event<GameInstanceLoadCallback> EVENT = EventFactory.createArrayBacked(GameInstanceLoadCallback.class,
            (listeners) -> (instance) -> {
                for (GameInstanceLoadCallback listener : listeners) {
                    listener.load(instance);
                }
            });

    void load(ConquestInstance instance);

}
