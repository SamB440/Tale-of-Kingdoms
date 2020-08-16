package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldSessionStartCallback {

    Event<WorldSessionStartCallback> EVENT = EventFactory.createArrayBacked(WorldSessionStartCallback.class,
            (listeners) -> (worldName) -> {
                for (WorldSessionStartCallback listener : listeners) {
                    listener.start(worldName);
                }
            });

    void start(String worldName);

}
