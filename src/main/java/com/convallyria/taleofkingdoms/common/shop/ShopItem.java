package com.convallyria.taleofkingdoms.common.shop;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public abstract class ShopItem {

    public abstract int getCost();

    public abstract Item getItem();

    public abstract String getName();

    public boolean canBuy(ConquestInstance instance, PlayerEntity player, int count) {
        return instance.getCoins(player.getUuid()) >= (getCost() * count);
    }
}
