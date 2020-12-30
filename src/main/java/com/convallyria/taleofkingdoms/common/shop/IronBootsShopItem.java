package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronBootsShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 576;
    }

    @Override
    public Item getItem() {
        return Items.IRON_BOOTS;
    }

    @Override
    public String getName() {
        return "Iron Boots";
    }
}
