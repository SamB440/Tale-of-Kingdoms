package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ArrowShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 7;
    }

    @Override
    public Item getItem() {
        return Items.ARROW;
    }

    @Override
    public String getName() {
        return "Arrow";
    }
}
