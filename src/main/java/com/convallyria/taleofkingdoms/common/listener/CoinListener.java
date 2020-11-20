package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import com.convallyria.taleofkingdoms.common.event.ItemMergeCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {

    public CoinListener() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                PlayerEntity playerEntity = null;
                System.out.println("death");
                if (entity instanceof PlayerEntity) {
                    System.out.println("instanceof");
                    if (instance instanceof ClientConquestInstance) {
                        System.out.println("client");
                        ClientConquestInstance clientConquestInstance = (ClientConquestInstance) instance;
                        int subtract = (clientConquestInstance.getCoins() / 20) * 100;
                        clientConquestInstance.setCoins(clientConquestInstance.getCoins() - subtract);
                    } else {
                        ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                        int subtract = (serverConquestInstance.getCoins(entity.getUuid()) / 20) * 100;
                        serverConquestInstance.setCoins(entity.getUuid(), subtract);
                    }
                    return;
                }

                if (source.getSource() instanceof PlayerEntity
                        || source.getSource() instanceof HunterEntity
                        || source.getSource() instanceof ProjectileEntity) {
                    if (source.getSource() instanceof ProjectileEntity) {
                        ProjectileEntity projectileEntity = (ProjectileEntity) source.getSource();
                        if (projectileEntity.getOwner() instanceof PlayerEntity) {
                            playerEntity = (PlayerEntity) projectileEntity.getOwner();
                        }

                        if (!(projectileEntity.getOwner() instanceof PlayerEntity)
                                && !(projectileEntity.getOwner() instanceof HunterEntity)) {
                            return;
                        }
                    } else if (source.getSource() instanceof PlayerEntity) {
                        playerEntity = (PlayerEntity) source.getSource();
                    }

                    //TODO assosciate owner with hunter entity
                    ItemHelper.dropCoins(entity);
                    if (instance instanceof ClientConquestInstance) {
                        ClientConquestInstance clientConquestInstance = (ClientConquestInstance) instance;
                        clientConquestInstance.setWorthiness(clientConquestInstance.getWorthiness() + 1);
                    } else if (playerEntity instanceof ServerPlayerEntity) {
                        ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                        serverConquestInstance.setWorthiness(playerEntity.getUuid(), serverConquestInstance.getWorthiness(playerEntity.getUuid()) + 1);
                        serverConquestInstance.sync((ServerPlayerEntity) playerEntity, false, null); //TODO put this somewhere else
                    }
                }
            });
        });

        EntityPickupItemCallback.EVENT.register((player, item) -> {
            if (equalsCoin(item)) {
                TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                    Random random = ThreadLocalRandom.current();
                    if (instance instanceof ClientConquestInstance) {
                        ((ClientConquestInstance) instance).addCoins(random.nextInt(50));
                    } else {
                        ((ServerConquestInstance) instance).addCoins(player.getUuid(), random.nextInt(50));
                        ((ServerConquestInstance) instance).sync((ServerPlayerEntity) player, false, null);
                    }
                });

                player.inventory.remove(predicate -> predicate.getItem().equals(item.getItem()), -1, player.inventory);
            }
        });

        ItemMergeCallback.EVENT.register((stack1, stack2) -> !equalsCoin(stack1) || !equalsCoin(stack2));
    }

    private boolean equalsCoin(ItemStack stack) {
        return stack.getItem().equals(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN));
    }
}
