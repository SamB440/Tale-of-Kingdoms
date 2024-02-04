package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingOpenScreenPacketHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final PlayerKingdom kingdom = guildPlayer.getKingdom();

        final long day = player.getWorld().getTimeOfDay() / 24000L;
        if (kingdom != null && kingdom.getLastStockMarketUpdate() < day) {
            // Update all shop item modifiers if a day has passed
            // Wow this stock market is all over the place
            for (ShopItem shopItem : ShopParser.SHOP_ITEMS.get(getGUIType())) {
                //todo: figure out a better formula
                //todo: how to sync to client?
                shopItem.setModifier(ThreadLocalRandom.current().nextDouble(0.75, 3));
            }
            kingdom.setLastStockMarketUpdate(day);
        }

        if (hand == Hand.OFF_HAND || player.getWorld().isClient()) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getPacketHandler(Packets.OPEN_CLIENT_SCREEN).handleOutgoingPacket(player, OutgoingOpenScreenPacketHandler.ScreenTypes.STOCK_MARKET, this.getId());
        return ActionResult.PASS;
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
