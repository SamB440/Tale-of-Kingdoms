package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuyItemPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class IncomingBuyItemPacketHandler extends InServerPacketHandler<BuyItemPacket> {

    public IncomingBuyItemPacketHandler() {
        super(Packets.BUY_ITEM, BuyItemPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, BuyItemPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        String itemName = packet.itemName();
        int count = packet.count();
        ShopParser.GUI type = packet.guiType();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for either foodshop, itemshop, or blacksmith in the guild
            Optional<? extends ShopEntity> entity = Optional.empty();
            switch (type) {
                case BLACKSMITH -> entity = instance.search(player, player.level(), EntityTypes.BLACKSMITH);
                case FOOD -> entity = instance.search(player, player.level(), EntityTypes.FOODSHOP);
                case ITEM -> entity = instance.search(player, player.level(), EntityTypes.ITEM_SHOP);
            }

            if (entity.isEmpty()) {
                reject(player, "Shop entity not present in guild.");
                return;
            }

            ShopItem shopItem = getShopItem(itemName, entity.get());
            if (shopItem == null) {
                reject(player, "Shop item not found.");
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player);
            int cost = shopItem.getCost() * count;
            if (guildPlayer.getCoins() < cost) {
                reject(player, "Coins requirement not met.");
                return;
            }

            guildPlayer.setCoins(guildPlayer.getCoins() - cost);
            // Only give item after coins have been deducted. This means they cannot infinitely get items if our setCoins method is broken.
            player.getInventory().add(new ItemStack(shopItem.getItem(), count));
            ServerConquestInstance.sync(player, instance);
        }));
    }

    private ShopItem getShopItem(String name, ShopEntity entity) {
        for (ShopItem shopItem : entity.getShopItems()) {
            if (BuiltInRegistries.ITEM.getKey(shopItem.getItem().asItem()).toString().equals(name)) return shopItem;
        }
        return null; // Nothing found :(
    }
}
