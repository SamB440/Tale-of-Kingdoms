package com.convallyria.taleofkingdoms.client.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ShopBuyUtil {

    public static void buyItem(ConquestInstance instance, Player player, ShopItem shopItem, int count) {
        if (shopItem.canBuy(instance, player, count)) {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.executeOnMain(() -> {
                    MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
                    if (server == null) {
                        api.getClientHandler(TaleOfKingdoms.BUY_ITEM_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BUY_ITEM_PACKET_ID,
                                        player,
                                        null, shopItem.getName(), count);
                        return;
                    }

                    ServerPlayer serverPlayerEntity = server.getPlayerList().getPlayer(player.getUUID());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.getInventory().add(new ItemStack(shopItem.getItem(), count));
                        int cost = shopItem.getCost() * count;
                        instance.setCoins(instance.getCoins() - cost);
                    }
                });
            });
        }
    }
}
