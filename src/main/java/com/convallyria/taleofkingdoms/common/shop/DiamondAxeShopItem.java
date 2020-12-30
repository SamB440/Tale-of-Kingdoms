package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondAxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 2484;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_AXE;
    }

    @Override
    public String getName() {
        return "Diamond Axe";
    }
}
