package com.convallyria.taleofkingdoms.common.shop;

import net.minecraft.item.Item;

public abstract class ShopItem {

    public abstract int getCost();

    public abstract Item getItem();

    public abstract String getName();
}
