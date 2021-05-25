package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class StoneAxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public Item getItem() {
        return Items.STONE_AXE;
    }

    @Override
    public String getName() {
        return "Stone Axe";
    }
}
