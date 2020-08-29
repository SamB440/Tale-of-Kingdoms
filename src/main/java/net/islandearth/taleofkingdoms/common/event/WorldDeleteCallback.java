package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldDeleteCallback {

    Event<WorldDeleteCallback> EVENT = EventFactory.createArrayBacked(WorldDeleteCallback.class,
            (listeners) -> (name) -> {
                for (WorldDeleteCallback listener : listeners) {
                    listener.delete(name);
                }
            });

    void delete(String name);
}
