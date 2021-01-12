package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class GoldenAppleShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 5000;
    }

    @Override
    public Item getItem() {
        return Items.GOLDEN_APPLE;
    }

    @Override
    public String getName() {
        return "Golden Apple";
    }
}

