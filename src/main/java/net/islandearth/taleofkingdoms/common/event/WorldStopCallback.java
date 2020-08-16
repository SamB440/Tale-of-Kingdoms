package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldStopCallback {

    Event<WorldStopCallback> EVENT = EventFactory.createArrayBacked(WorldStopCallback.class,
            (listeners) -> () -> {
                for (WorldStopCallback listener : listeners) {
                    listener.stop();
                }
            });

    void stop();
}
