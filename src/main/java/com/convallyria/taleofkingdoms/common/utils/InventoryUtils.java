package com.convallyria.taleofkingdoms.common.utils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InventoryUtils {

    @Nullable
    public static ItemStack getStack(PlayerInventory playerInventory, List<Item> validItems, int count) {
        for (ItemStack itemStack : playerInventory.main) {
            if (validItems.contains(itemStack.getItem())) {
                if (itemStack.getCount() == count) {
                    return itemStack;
                }
            }
        }
        return null;
    }

    public static int getSlotWithStack(PlayerInventory playerInventory, ItemStack stack) {
        for (int i = 0; i < playerInventory.main.size(); ++i) {
            if (!playerInventory.main.get(i).isEmpty() && areItemsEqual(stack, playerInventory.main.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areNbtEqual(stack1, stack2);
    }
}
