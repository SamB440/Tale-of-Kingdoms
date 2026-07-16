package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface EntitySpawnCallback {

    Event<EntitySpawnCallback> EVENT = EventFactory.of(
            (listeners) -> entity -> {
                for (EntitySpawnCallback listener : listeners) {
                    return listener.spawn(entity);
                }
                return true;
            });

    boolean spawn(Entity entity);
}
