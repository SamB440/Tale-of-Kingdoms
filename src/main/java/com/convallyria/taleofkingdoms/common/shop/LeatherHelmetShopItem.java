package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class LeatherHelmetShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 135;
    }

    @Override
    public Item getItem() {
        return Items.LEATHER_HELMET;
    }

    @Override
    public String getName() {
        return "Leather Helmet";
    }
}
