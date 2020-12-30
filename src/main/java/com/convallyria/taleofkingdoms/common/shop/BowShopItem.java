package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class BowShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 450;
    }

    @Override
    public Item getItem() {
        return Items.BOW;
    }

    @Override
    public String getName() {
        return "Bow";
    }
}
