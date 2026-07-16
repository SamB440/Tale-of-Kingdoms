package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface EntityPickupItemCallback {

    Event<EntityPickupItemCallback> EVENT = EventFactory.of(
            (listeners) -> (entity, item) -> {
                for (EntityPickupItemCallback listener : listeners) {
                    listener.pickup(entity, item);
                }
            });

    void pickup(Player player, ItemStack item);
}
