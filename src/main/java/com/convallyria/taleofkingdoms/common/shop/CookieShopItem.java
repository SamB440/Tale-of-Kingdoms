package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CookieShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 18;
    }

    @Override
    public Item getItem() {
        return Items.COOKIE;
    }

    @Override
    public String getName() {
        return "Cookie";
    }
}

