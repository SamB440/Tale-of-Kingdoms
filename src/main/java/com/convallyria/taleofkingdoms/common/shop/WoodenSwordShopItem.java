package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class WoodenSwordShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 9;
    }

    @Override
    public Item getItem() {
        return Items.WOODEN_SWORD;
    }

    @Override
    public String getName() {
        return "Wooden Sword";
    }
}
