package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class RawBeefShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 35;
    }

    @Override
    public Item getItem() {
        return Items.BEEF;
    }

    @Override
    public String getName() {
        return "Raw Beef";
    }
}

