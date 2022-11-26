package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        String playerContext = identifier.toString() + " @ <" + player.getName().getString() + ":" + player.getIp() + ">";
        String itemName = attachedData.readString(128);
        int count = attachedData.readInt();
        ShopParser.GUI type = attachedData.readEnumConstant(ShopParser.GUI.class);
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Not in guild.");
                    return;
                }

                // Search for either foodshop, itemshop, or blacksmith in the guild
                Optional<? extends ShopEntity> entity = Optional.empty();
                switch (type) {
                    case BLACKSMITH -> entity = instance.getGuildEntity(player.world, EntityTypes.BLACKSMITH);
                    case FOOD -> entity = instance.getGuildEntity(player.world, EntityTypes.FOODSHOP);
                    case ITEM -> entity = instance.getGuildEntity(player.world, EntityTypes.ITEM_SHOP);
                }

                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Shop entity not present in guild.");
                    return;
                }

                ShopItem shopItem = getShopItem(itemName, entity.get());
                if (shopItem == null) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Shop item not found.");
                    return;
                }

                int cost = shopItem.getCost() * count;
                if (instance.getCoins(player.getUuid()) < cost) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Coins requirement not met.");
                    return;
                }

                instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - cost);
                // Only give item after coins have been deducted. This means they cannot infinitely get items if our setCoins method is broken.
                player.getInventory().insertStack(new ItemStack(shopItem.getItem(), count));
                ServerConquestInstance.sync(player, instance);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }

    private ShopItem getShopItem(String name, ShopEntity entity) {
        for (ShopItem shopItem : entity.getShopItems()) {
            if (shopItem.getName().equals(name)) return shopItem;
        }
        return null; // Nothing found :(
    }
}
