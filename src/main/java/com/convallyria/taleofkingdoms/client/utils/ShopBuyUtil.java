package com.convallyria.taleofkingdoms.client.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@Environment(EnvType.CLIENT)
public class ShopBuyUtil {

    public static void buyItem(ConquestInstance instance, PlayerEntity player, ShopItem shopItem, int count) {
        if (shopItem.canBuy(instance, player, count)) {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.executeOnMain(() -> {
                MinecraftServer server = MinecraftClient.getInstance().getServer();
                if (server == null) {
                    api.getClientHandler(TaleOfKingdoms.BUY_ITEM_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.BUY_ITEM_PACKET_ID,
                                    player, shopItem.getName(), count);
                    return;
                }

                ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                if (serverPlayerEntity != null) {
                    final ItemStack stack = new ItemStack(shopItem.getItem(), count);
                    final PlayerInventory inventory = serverPlayerEntity.getInventory();
                    int slotWithRoom = inventory.getOccupiedSlotWithRoomForStack(stack);
                    // Try to get an empty slot instead...
                    if (slotWithRoom == -1) slotWithRoom = inventory.getEmptySlot();
                    if (slotWithRoom == -1) {
                        serverPlayerEntity.dropItem(stack, true, true);
                    } else {
                        inventory.insertStack(slotWithRoom, stack);
                    }
                    int cost = shopItem.getCost() * count;
                    instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - cost);
                }
            });
        }
    }
}
