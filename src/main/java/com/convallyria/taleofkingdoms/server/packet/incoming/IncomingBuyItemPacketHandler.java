package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingBuyItemPacketHandler extends ServerPacketHandler {

    public IncomingBuyItemPacketHandler() {
        super(Packets.BUY_ITEM);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        String itemName = attachedData.readString(128);
        int count = attachedData.readInt();
        ShopParser.GUI type = attachedData.readEnumConstant(ShopParser.GUI.class);
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for either foodshop, itemshop, or blacksmith in the guild
            Optional<? extends ShopEntity> entity = Optional.empty();
            switch (type) {
                case BLACKSMITH -> entity = instance.search(player, player.getWorld(), EntityTypes.BLACKSMITH);
                case FOOD -> entity = instance.search(player, player.getWorld(), EntityTypes.FOODSHOP);
                case ITEM -> entity = instance.search(player, player.getWorld(), EntityTypes.ITEM_SHOP);
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
            player.getInventory().insertStack(new ItemStack(shopItem.getItem(), count));
            ServerConquestInstance.sync(player, instance);
        }));
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }

    private ShopItem getShopItem(String name, ShopEntity entity) {
        for (ShopItem shopItem : entity.getShopItems()) {
            if (shopItem.getName().equals(name)) return shopItem;
        }
        return null; // Nothing found :(
    }
}
