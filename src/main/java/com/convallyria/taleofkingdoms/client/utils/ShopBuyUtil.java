package com.convallyria.taleofkingdoms.client.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@Environment(EnvType.CLIENT)
public class ShopBuyUtil {

    public static void buyItem(ConquestInstance instance, PlayerEntity player, ShopItem shopItem) {
        if (shopItem.canBuy(instance, player)) {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.executeOnMain(() -> {
                    MinecraftServer server = MinecraftClient.getInstance().getServer();
                    if (server == null) {
                        api.getClientHandler(TaleOfKingdoms.BUY_ITEM_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BUY_ITEM_PACKET_ID,
                                        player,
                                        null, shopItem.getName());
                        return;
                    }

                    ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.inventory.insertStack(new ItemStack(shopItem.getItem(), 1));
                        instance.setCoins(instance.getCoins() - shopItem.getCost());
                    }
                });
            });
        }
    }
}
