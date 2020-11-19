package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface InventoryInsertCallback {

    Event<InventoryInsertCallback> EVENT = EventFactory.createArrayBacked(InventoryInsertCallback.class,
            (listeners) -> (player, slot, stack) -> {
                for (InventoryInsertCallback listener : listeners) {
                    return listener.insertStack(player, slot, stack);
                }
                return true;
            });

    boolean insertStack(PlayerEntity player, int slot, ItemStack stack);
}
