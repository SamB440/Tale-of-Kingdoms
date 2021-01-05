package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondBootsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 2538;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_BOOTS;
    }

    @Override
    public String getName() {
        return "Diamond Boots";
    }
}
