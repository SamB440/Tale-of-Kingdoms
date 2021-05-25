package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class WoodenShovelShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 10;
    }

    @Override
    public Item getItem() {
        return Items.WOODEN_SHOVEL;
    }

    @Override
    public String getName() {
        return "Wooden Shovel";
    }
}
