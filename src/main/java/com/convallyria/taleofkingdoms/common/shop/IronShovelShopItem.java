package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class IronShovelShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 156;
    }

    @Override
    public Item getItem() {
        return Items.IRON_SHOVEL;
    }

    @Override
    public String getName() {
        return "Iron Shovel";
    }
}
