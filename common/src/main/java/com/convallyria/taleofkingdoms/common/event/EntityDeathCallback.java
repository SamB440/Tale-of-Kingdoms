package com.convallyria.taleofkingdoms.common.event;


import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface EntityDeathCallback {

    Event<EntityDeathCallback> EVENT = EventFactory.of(
            (listeners) -> (source, entity) -> {
                for (EntityDeathCallback listener : listeners) {
                    listener.death(source, entity);
                }
            });

    void death(DamageSource source, @NotNull Entity entity);
}
