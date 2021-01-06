package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class BeetrootShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 42;
    }

    @Override
    public Item getItem() {
        return Items.BEETROOT;
    }

    @Override
    public String getName() {
        return "Beetroot";
    }
}

