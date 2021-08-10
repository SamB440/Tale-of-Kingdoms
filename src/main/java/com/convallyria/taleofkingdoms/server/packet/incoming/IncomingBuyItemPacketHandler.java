package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingBuyItemPacketHandler extends ServerPacketHandler {

    public IncomingBuyItemPacketHandler() {
        super(TaleOfKingdoms.BUY_ITEM_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context.player();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        String itemName = attachedData.readUtf(32367);
        int count = attachedData.readInt();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for either foodshop or blacksmith in the guild
                Optional<? extends Entity> entity = instance.getGuildEntity(player.level, EntityTypes.BLACKSMITH);
                if (entity.isEmpty()) entity = instance.getGuildEntity(player.level, EntityTypes.FOODSHOP);
                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Shop entity not present in guild.");
                    return;
                }

                ShopItem shopItem = getShopItem(itemName);
                if (shopItem == null) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Shop item not found.");
                    return;
                }

                int cost = shopItem.getCost() * count;
                if (instance.getCoins(player.getUUID()) < cost) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Coins requirement not met.");
                    return;
                }

                instance.setCoins(player.getUUID(), instance.getCoins(player.getUUID()) - cost);
                // Only give item after coins have been deducted. This means they cannot infinitely get items if our setCoins method is broken.
                player.getInventory().add(new ItemStack(shopItem.getItem(), count));
                instance.sync(player, null);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player,
                                     @Nullable Connection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }

    private ShopItem getShopItem(String name) {
        for (ShopItem blacksmithShopItem : BlacksmithEntity.getBlacksmithShopItems()) {
            if (blacksmithShopItem.getName().equals(name)) return blacksmithShopItem;
        }
        // Couldn't find it in blacksmith items, try the food shop.
        for (ShopItem foodShopItem : FoodShopEntity.getFoodShopItems()) {
            if (foodShopItem.getName().equals(name)) return foodShopItem;
        }
        return null; // Nothing found :(
    }
}
