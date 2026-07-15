package com.convallyria.taleofkingdoms.common.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class InventoryUtils {

    @Nullable
    public static ItemStack getStack(Inventory playerInventory, TagKey<Item> tag, int count) {
        for (ItemStack itemStack : playerInventory.items) {
            if (itemStack.getItem().builtInRegistryHolder().is(tag)) { //todo for 1.18.2: check if this works
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

    public static int getSlotWithStack(SimpleContainer inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            if (!inventory.getItem(i).isEmpty() && areItemsEqual(stack, inventory.getItem(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    public static int count(Container inventory, TagKey<Item> tag) {
        int i = 0;
        for (int j = 0; j < inventory.getContainerSize(); ++j) {
            ItemStack itemStack = inventory.getItem(j);
            if (!itemStack.getItem().builtInRegistryHolder().is(tag)) continue;
            i += itemStack.getCount();
        }
        return i;
    }
}
