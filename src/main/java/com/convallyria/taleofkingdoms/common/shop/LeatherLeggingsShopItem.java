package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class LeatherLeggingsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 162;
    }

    @Override
    public Item getItem() {
        return Items.LEATHER_LEGGINGS;
    }

    @Override
    public String getName() {
        return "Leather Leggings";
    }
}
