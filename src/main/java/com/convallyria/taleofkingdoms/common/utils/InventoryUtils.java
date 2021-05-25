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
}
