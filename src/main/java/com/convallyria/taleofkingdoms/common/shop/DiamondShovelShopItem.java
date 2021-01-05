package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DiamondShovelShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 831;
    }

    @Override
    public Item getItem() {
        return Items.DIAMOND_SHOVEL;
    }

    @Override
    public String getName() {
        return "Diamond Shovel";
    }
}
