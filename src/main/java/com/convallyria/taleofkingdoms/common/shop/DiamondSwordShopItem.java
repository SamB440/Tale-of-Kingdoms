package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondSwordShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 1539;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_SWORD;
    }

    @Override
    public String getName() {
        return "Diamond Sword";
    }
}
