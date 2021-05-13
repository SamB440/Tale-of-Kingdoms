package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import com.convallyria.taleofkingdoms.common.event.ItemMergeCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {

    public CoinListener() {
        dropCoinsOnDeath();
        coinPickup();
        preventCoinMerge();
    }

    private void dropCoinsOnDeath() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                PlayerEntity playerEntity = null;
                if (entity instanceof PlayerEntity) {
                    int subtract = (instance.getCoins() / 20);
                    instance.setCoins(entity.getUuid(), instance.getCoins() - subtract);
                    return;
                }

                if (source.getSource() instanceof PlayerEntity
                        || source.getSource() instanceof HunterEntity
                        || source.getSource() instanceof GuildGuardEntity
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

                    //TODO associate owner with hunter entity
                    ItemHelper.dropCoins(entity);
                    instance.setWorthiness(source.getSource().getUuid(), instance.getWorthiness(source.getSource().getUuid()) + 1);
                    if (playerEntity instanceof ServerPlayerEntity) {
                        System.out.println("ATTACK!");
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                        instance.attack(serverPlayerEntity, serverPlayerEntity.getServerWorld());
                    }

                    if (instance instanceof ServerConquestInstance) {
                        ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                        serverConquestInstance.sync((ServerPlayerEntity) playerEntity, null);
                    }
                }
            });
        });
    }

    private void coinPickup() {
        EntityPickupItemCallback.EVENT.register((player, item) -> {
            if (equalsCoin(item)) {
                TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                    Random random = ThreadLocalRandom.current();
                    instance.addCoins(player.getUuid(), random.nextInt(10));
                    if (instance instanceof ServerConquestInstance) {
                        ((ServerConquestInstance) instance).sync((ServerPlayerEntity) player, null);
                    }
                });

                player.inventory.remove(predicate -> predicate.getItem().equals(item.getItem()), -1, player.inventory);
            }
        });
    }

    private void preventCoinMerge() {
        ItemMergeCallback.EVENT.register((stack1, stack2) -> !equalsCoin(stack1) || !equalsCoin(stack2));
    }

    private boolean equalsCoin(ItemStack stack) {
        return stack.getItem().equals(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN));
    }
}
