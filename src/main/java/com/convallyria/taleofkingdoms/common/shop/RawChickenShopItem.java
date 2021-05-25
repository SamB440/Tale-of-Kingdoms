package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class RawChickenShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public Item getItem() {
        return Items.CHICKEN;
    }

    @Override
    public String getName() {
        return "Raw Chicken";
    }
}
