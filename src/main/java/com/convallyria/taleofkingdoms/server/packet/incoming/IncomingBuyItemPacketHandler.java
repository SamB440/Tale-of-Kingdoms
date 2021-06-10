package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingBuyItemPacketHandler extends ServerPacketHandler {

    public IncomingBuyItemPacketHandler() {
        super(TaleOfKingdoms.BUY_ITEM_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        String playerContext = " @ <" + player.getName().asString() + ":" + player.getIp() + ">";
        String itemName = attachedData.readString(32367);
        int count = attachedData.readInt();
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for either foodshop or blacksmith in the guild
                Optional<? extends Entity> entity = instance.getGuildEntity(player.world, EntityTypes.BLACKSMITH);
                if (entity.isEmpty()) entity = instance.getGuildEntity(player.world, EntityTypes.FOODSHOP);
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
                if (instance.getCoins(player.getUuid()) < cost) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Coins requirement not met.");
                    return;
                }

                instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - cost);
                // Only give item after coins have been deducted. This means they cannot infinitely get items if our setCoins method is broken.
                player.getInventory().insertStack(new ItemStack(shopItem.getItem(), count));
                instance.sync(player, null);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
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
