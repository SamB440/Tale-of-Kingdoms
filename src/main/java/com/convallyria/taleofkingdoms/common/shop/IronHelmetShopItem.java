package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronHelmetShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 765;
    }

    @Override
    public Item getItem() {
        return Items.IRON_HELMET;
    }

    @Override
    public String getName() {
        return "Iron Helmet";
    }
}
