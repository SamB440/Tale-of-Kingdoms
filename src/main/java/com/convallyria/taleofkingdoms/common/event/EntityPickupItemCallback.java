package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface EntityPickupItemCallback {

    Event<EntityPickupItemCallback> EVENT = EventFactory.createArrayBacked(EntityPickupItemCallback.class,
            (listeners) -> (entity, item) -> {
                for (EntityPickupItemCallback listener : listeners) {
                    listener.pickup(entity, item);
                }
            });

    void pickup(PlayerEntity player, ItemStack item);
}
