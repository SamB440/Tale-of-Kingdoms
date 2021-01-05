package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondChestplateShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 6091;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_CHESTPLATE;
    }

    @Override
    public String getName() {
        return "Diamond Chestplate";
    }
}
