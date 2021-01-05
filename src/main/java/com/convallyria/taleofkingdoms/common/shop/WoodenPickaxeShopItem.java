package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class WoodenPickaxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 14;
    }

    @Override
    public Item getItem() {
        return Items.WOODEN_PICKAXE;
    }

    @Override
    public String getName() {
        return "Wooden Pickaxe";
    }
}
