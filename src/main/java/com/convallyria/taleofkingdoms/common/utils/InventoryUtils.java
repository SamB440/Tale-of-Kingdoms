package com.convallyria.taleofkingdoms.common.utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InventoryUtils {

    @Nullable
    public static ItemStack getStack(Inventory playerInventory, List<Item> validItems, int count) {
        for (ItemStack itemStack : playerInventory.items) {
            if (validItems.contains(itemStack.getItem())) {
                if (itemStack.getCount() == count) {
                    return itemStack;
                }
            }
        }
        return null;
    }

    public static int getSlotWithStack(Inventory playerInventory, ItemStack stack) {
        for (int i = 0; i < playerInventory.items.size(); ++i) {
            if (!playerInventory.items.get(i).isEmpty() && areItemsEqual(stack, playerInventory.items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }
}
