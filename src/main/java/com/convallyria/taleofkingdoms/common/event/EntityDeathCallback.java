package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityDeathCallback {

    Event<EntityDeathCallback> EVENT = EventFactory.createArrayBacked(EntityDeathCallback.class,
            (listeners) -> (source, entity) -> {
                for (EntityDeathCallback listener : listeners) {
                    listener.death(source, entity);
                }
            });

    void death(DamageSource source, LivingEntity entity);
}
