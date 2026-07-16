package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.item.ItemStack;

public interface ItemMergeCallback {

    Event<ItemMergeCallback> EVENT = EventFactory.of(
            (listeners) -> (stack1, stack2) -> {
                for (ItemMergeCallback listener : listeners) {
                    return listener.tryMerge(stack1, stack2);
                }
                return true;
            });

    boolean tryMerge(ItemStack stack1, ItemStack stack2);
}
