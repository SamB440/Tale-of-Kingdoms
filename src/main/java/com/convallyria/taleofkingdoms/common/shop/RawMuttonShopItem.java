package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class RawMuttonShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 35;
    }

    @Override
    public Item getItem() {
        return Items.MUTTON;
    }

    @Override
    public String getName() {
        return "Raw Mutton";
    }
}
