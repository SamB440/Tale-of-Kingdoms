package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

public interface EntityPickupItemCallback {

    Event<EntityPickupItemCallback> EVENT = EventFactory.createArrayBacked(EntityPickupItemCallback.class,
            (listeners) -> (entity, item, count) -> {
                for (EntityPickupItemCallback listener : listeners) {
                    listener.pickup(entity, item, count);
                }
            });

    void pickup(Entity entity, Entity item, int count);
}
