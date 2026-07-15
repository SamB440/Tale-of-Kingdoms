package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class StockMarketEntity extends ShopEntity {

    public StockMarketEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public ShopParser.GUI getGUIType() {
        return ShopParser.GUI.STOCK_MARKET;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 100F));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Check if there is at least 1 Minecraft day difference
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return InteractionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return InteractionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final PlayerKingdom kingdom = guildPlayer.getKingdom();

        final long day = player.level().getDayTime() / 24000L;
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

        if (hand == InteractionHand.OFF_HAND || player.level().isClientSide()) return InteractionResult.FAIL;
        TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN).sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.STOCK_MARKET, this.getId()));
        return InteractionResult.PASS;
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
