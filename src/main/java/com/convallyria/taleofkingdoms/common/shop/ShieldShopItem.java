package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ShieldShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 856;
    }

    @Override
    public Item getItem() {
        return Items.SHIELD;
    }

    @Override
    public String getName() {
        return "Shield";
    }
}
