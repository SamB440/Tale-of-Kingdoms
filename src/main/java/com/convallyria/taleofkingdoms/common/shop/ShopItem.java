package com.convallyria.taleofkingdoms.common.shop;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class ShopItem {

    private final Item item;
    private final int cost;
    private final int sell;
    private double modifier;

    public ShopItem(Item item, int cost, int sell) {
        this.item = item;
        this.cost = cost;
        this.sell = sell;
        this.modifier = 1;
    }

    public Item getItem() {
        return item;
    }

    public int getCost() {
        return Math.max(1, (int) (cost * modifier));
    }

    public int getSell() {
        return sell;
    }

    public double getModifier() {
        return modifier;
    }

    public void setModifier(double modifier) {
        this.modifier = modifier;
    }

    public boolean canBuy(ConquestInstance instance, PlayerEntity player, int count) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        return guildPlayer.getCoins() >= ((getCost() * modifier) * count);
    }

    @Override
    public String toString() {
        return "ShopItem{" +
                "item=" + item +
                ", cost=" + cost +
                ", sell=" + sell +
                ", modifier=" + modifier +
                '}';
    }
}
