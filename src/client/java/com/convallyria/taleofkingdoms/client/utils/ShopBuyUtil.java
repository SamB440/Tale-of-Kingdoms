package com.convallyria.taleofkingdoms.client.utils;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClientAPI;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@Environment(EnvType.CLIENT)
public class ShopBuyUtil {

    public static void buyItem(ConquestInstance instance, PlayerEntity player, ShopItem shopItem, int count, ShopEntity entity) {
        if (shopItem.canBuy(instance, player, count)) {
            final TaleOfKingdomsClientAPI api = TaleOfKingdomsClient.getAPI();
            api.executeOnMain(() -> {
                MinecraftServer server = MinecraftClient.getInstance().getServer();
                if (server == null) {
                    api.getClientPacketHandler(Packets.BUY_ITEM)
                            .handleOutgoingPacket(player, Registries.ITEM.getId(shopItem.getItem().asItem()).toString(), count, entity.getGUIType());
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
                    final GuildPlayer guildPlayer = instance.getPlayer(player);
                    guildPlayer.setCoins(guildPlayer.getCoins() - cost);
                }
            });
        }
    }
}
