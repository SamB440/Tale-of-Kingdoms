package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondHelmetShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 3807;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_HELMET;
    }

    @Override
    public String getName() {
        return "Diamond Helmet";
    }
}
