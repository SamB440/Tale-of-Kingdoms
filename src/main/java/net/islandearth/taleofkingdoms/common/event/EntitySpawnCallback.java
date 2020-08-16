package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

public interface EntitySpawnCallback {

    Event<EntitySpawnCallback> EVENT = EventFactory.createArrayBacked(EntitySpawnCallback.class,
            (listeners) -> entity -> {
                for (EntitySpawnCallback listener : listeners) {
                    return listener.spawn(entity);
                }
                return true;
            });

    boolean spawn(Entity entity);
}
