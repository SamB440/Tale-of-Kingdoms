package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface EntityPickupItemCallback {

    Event<EntityPickupItemCallback> EVENT = EventFactory.createArrayBacked(EntityPickupItemCallback.class,
            (listeners) -> (entity, item) -> {
                for (EntityPickupItemCallback listener : listeners) {
                    listener.pickup(entity, item);
                }
            });

    void pickup(Player player, ItemStack item);
}
