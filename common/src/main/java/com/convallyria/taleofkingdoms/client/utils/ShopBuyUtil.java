package com.convallyria.taleofkingdoms.client.utils;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClientAPI;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuyItemPacket;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ShopBuyUtil {

    public static void buyItem(ConquestInstance instance, Player player, ShopItem shopItem, int count, ShopEntity entity) {
        if (shopItem.canBuy(instance, player, count)) {
            final TaleOfKingdomsClientAPI api = TaleOfKingdomsClient.getAPI();
            api.executeOnMain(() -> {
                MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
                if (server == null) {
                    api.getClientPacket(Packets.BUY_ITEM)
                            .sendPacket(player, new BuyItemPacket(BuiltInRegistries.ITEM.getKey(shopItem.getItem().asItem()).toString(), count, entity.getGUIType()));
                    return;
                }

                ServerPlayer serverPlayerEntity = server.getPlayerList().getPlayer(player.getUUID());
                if (serverPlayerEntity != null) {
                    final ItemStack stack = new ItemStack(shopItem.getItem(), count);
                    final Inventory inventory = serverPlayerEntity.getInventory();
                    int slotWithRoom = inventory.getSlotWithRemainingSpace(stack);
                    // Try to get an empty slot instead...
                    if (slotWithRoom == -1) slotWithRoom = inventory.getFreeSlot();
                    if (slotWithRoom == -1) {
                        serverPlayerEntity.drop(stack, true, true);
                    } else {
                        inventory.add(slotWithRoom, stack);
                    }
                    int cost = shopItem.getCost() * count;
                    final GuildPlayer guildPlayer = instance.getPlayer(player);
                    guildPlayer.setCoins(guildPlayer.getCoins() - cost);
                }
            });
        }
    }
}
