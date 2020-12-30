package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondPickaxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 2484;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_PICKAXE;
    }

    @Override
    public String getName() {
        return "Diamond Pickaxe";
    }
}
