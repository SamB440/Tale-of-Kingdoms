package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class LeatherBootsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 108;
    }

    @Override
    public Item getItem() {
        return Items.LEATHER_BOOTS;
    }

    @Override
    public String getName() {
        return "Leather Boots";
    }
}
