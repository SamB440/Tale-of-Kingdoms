package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.StockMarketScreen;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class StockMarketEntity extends ShopEntity {

    public StockMarketEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ImmutableList<ShopItem> getShopItems() {
        return ImmutableList.copyOf(ShopParser.SHOP_ITEMS.get(ShopParser.GUI.STOCK_MARKET));
    }

    @Override
    public ShopParser.GUI getGUIType() {
        return ShopParser.GUI.STOCK_MARKET;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        // Check if there is at least 1 Minecraft day difference
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return ActionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return ActionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        UUID uuid = player.getUuid();
        long day = player.world.getTimeOfDay() / 24000L;
        if (instance.getLastStockMarketUpdate(uuid) < day) {
            // Update all shop item modifiers if a day has passed
            // Wow this stock market is all over the place
            for (ShopItem shopItem : ShopParser.SHOP_ITEMS.get(getGUIType())) {
                //todo: figure out a better formula
                shopItem.setModifier(ThreadLocalRandom.current().nextDouble(0.75, 3));
            }
            instance.setLastStockMarketUpdate(uuid, day);
        }

        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        this.openScreen(player, instance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ConquestInstance instance) {
        StockMarketScreen screen = new StockMarketScreen(player, this, instance);
        MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
