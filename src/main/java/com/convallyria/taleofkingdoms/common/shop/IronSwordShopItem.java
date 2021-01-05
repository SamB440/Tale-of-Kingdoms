package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronSwordShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 288;
    }

    @Override
    public Item getItem() {
        return Items.IRON_SWORD;
    }

    @Override
    public String getName() {
        return "Iron Sword";
    }
}
