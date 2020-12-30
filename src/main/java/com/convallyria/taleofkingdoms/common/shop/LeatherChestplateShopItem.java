package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class LeatherChestplateShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 216;
    }

    @Override
    public Item getItem() {
        return Items.LEATHER_CHESTPLATE;
    }

    @Override
    public String getName() {
        return "Leather Chestplate";
    }
}
