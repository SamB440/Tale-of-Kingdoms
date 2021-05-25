package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondLeggingsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 5329;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_LEGGINGS;
    }

    @Override
    public String getName() {
        return "Diamond Leggings";
    }
}
