package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CakeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 50;
    }

    @Override
    public Item getItem() {
        return Items.CAKE;
    }

    @Override
    public String getName() {
        return "Cake";
    }
}

