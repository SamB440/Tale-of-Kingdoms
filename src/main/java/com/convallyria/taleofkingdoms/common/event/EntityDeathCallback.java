package com.convallyria.taleofkingdoms.common.event;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.NotNull;

public interface EntityDeathCallback {

    Event<EntityDeathCallback> EVENT = EventFactory.createArrayBacked(EntityDeathCallback.class,
            (listeners) -> (source, entity) -> {
                for (EntityDeathCallback listener : listeners) {
                    listener.death(source, entity);
                }
            });

    void death(DamageSource source, @NotNull Entity entity);
}
