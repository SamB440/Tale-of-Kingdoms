package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface EntityBreedCallback {

    Event<EntityBreedCallback> EVENT = EventFactory.createArrayBacked(EntityBreedCallback.class,
            (listeners) -> (source, other, lovingPlayer) -> {
                for (EntityBreedCallback listener : listeners) {
                    listener.breed(source, other, lovingPlayer);
                }
            });

    void breed(AnimalEntity source, AnimalEntity other, PlayerEntity lovingPlayer);
}
