package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface EntityPickupItemCallback {

    Event<EntityPickupItemCallback> EVENT = EventFactory.createArrayBacked(EntityPickupItemCallback.class,
            (listeners) -> (entity, item) -> {
                for (EntityPickupItemCallback listener : listeners) {
                    return listener.pickup(entity, item);
                }
                return true;
            });

    boolean pickup(PlayerEntity player, ItemStack item);
}
