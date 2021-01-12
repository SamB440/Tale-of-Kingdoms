package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class BreadShopItem extends ShopItem {

    @Override
    public int getCost() { return 32; }

    @Override
    public Item getItem() {
        return Items.BREAD;
    }

    @Override
    public String getName() {
        return "Bread";
    }
}

