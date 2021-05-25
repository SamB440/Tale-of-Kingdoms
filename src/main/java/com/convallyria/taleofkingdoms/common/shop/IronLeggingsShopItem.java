package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronLeggingsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 1008;
    }

    @Override
    public Item getItem() {
        return Items.IRON_LEGGINGS;
    }

    @Override
    public String getName() {
        return "Iron Leggings";
    }
}
