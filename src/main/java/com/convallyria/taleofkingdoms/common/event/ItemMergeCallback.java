package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;

public interface ItemMergeCallback {

    Event<ItemMergeCallback> EVENT = EventFactory.createArrayBacked(ItemMergeCallback.class,
            (listeners) -> (stack1, stack2) -> {
                for (ItemMergeCallback listener : listeners) {
                    return listener.tryMerge(stack1, stack2);
                }
                return true;
            });

    boolean tryMerge(ItemStack stack1, ItemStack stack2);
}
