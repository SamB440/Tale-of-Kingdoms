package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronAxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 462;
    }

    @Override
    public Item getItem() {
        return Items.IRON_AXE;
    }

    @Override
    public String getName() {
        return "Iron Axe";
    }
}
