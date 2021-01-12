package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class RawPorkchopShopItem extends ShopItem {

    @Override
    public int getCost() {
        return 35;
    }

    @Override
    public Item getItem() { return Items.PORKCHOP; }

    @Override
    public String getName() { return "Raw Porkchop"; }
}
