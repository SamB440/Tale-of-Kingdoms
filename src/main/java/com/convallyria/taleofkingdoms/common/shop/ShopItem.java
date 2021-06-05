package com.convallyria.taleofkingdoms.common.shop;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class ShopItem {
    private final String name;
    private final Item item;
    private final int cost;
    private final int sell;

    public ShopItem(String name, Item item, int cost, int sell) {
        this.name = name;
        this.item = item;
        this.cost = cost;
        this.sell = sell;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public int getSell() {
        return sell;
    }

    public boolean canBuy(ConquestInstance instance, PlayerEntity player, int count) {
        return instance.getCoins(player.getUuid()) >= (getCost() * count);
    }

    @Override
    public String toString() {
        return "ShopItem{" +
                "name='" + name + '\'' +
                ", item=" + item +
                ", cost=" + cost +
                ", sell=" + sell +
                '}';
    }
}
