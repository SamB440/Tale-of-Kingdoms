package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class StonePickaxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public Item getItem() {
        return Items.STONE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Stone Pickaxe";
    }
}
