package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class StoneSwordShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 18;
    }

    @Override
    public Item getItem() {
        return Items.STONE_SWORD;
    }

    @Override
    public String getName() {
        return "Stone Sword";
    }
}
