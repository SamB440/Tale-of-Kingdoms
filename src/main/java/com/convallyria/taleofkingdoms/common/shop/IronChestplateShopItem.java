package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronChestplateShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 1071;
    }

    @Override
    public Item getItem() {
        return Items.IRON_CHESTPLATE;
    }

    @Override
    public String getName() {
        return "Iron Chestplate";
    }
}
