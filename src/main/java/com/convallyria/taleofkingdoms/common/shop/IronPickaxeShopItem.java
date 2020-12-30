package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronPickaxeShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 462;
    }

    @Override
    public Item getItem() {
        return Items.IRON_PICKAXE;
    }

    @Override
    public String getName() {
        return "Iron Pickaxe";
    }
}
