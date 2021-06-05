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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {
    private JsonObject worthinessJson;

    public CoinListener() {
        loadWorthinessJson();
        dropCoinsOnDeath();
        coinPickup();
        preventCoinMerge();
    }

    /**
     * When an entity dies this event is called.
     * If the entity is a player then the player's coins are subtracted by the amount of their current coins divided by 20 and the method returns.
     * If the cause of death was a player then the {@link LivingEntity} drops their coins
     * and the player gets worthiness added equal to the {@link CoinListener#getMobWorthiness(LivingEntity)} times (*) the {@link CoinListener#getDifficultyWorthinessMultiplier(World)}
     */
    private void dropCoinsOnDeath() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                PlayerEntity playerEntity = null;
                if (entity instanceof PlayerEntity) {
                    int subtract = (instance.getCoins(entity.getUuid()) / 20);
                    instance.setCoins(entity.getUuid(), instance.getCoins(entity.getUuid()) - subtract);
                    return;
                }

                if (source.getSource() instanceof PlayerEntity
                        || source.getSource() instanceof HunterEntity
                        || source.getSource() instanceof GuildGuardEntity
                        || source.getSource() instanceof ProjectileEntity) {
                    if (source.getSource() instanceof ProjectileEntity projectileEntity) {
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

                    if (source.getSource() instanceof PlayerEntity) {
                        instance.addWorthiness(source.getSource().getUuid(), getMobWorthiness(entity) * getDifficultyWorthinessMultiplier(source.getSource().world));
                    }

                    if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                        instance.attack(serverPlayerEntity, serverPlayerEntity.getServerWorld());
                    }

                    if (instance instanceof ServerConquestInstance serverConquestInstance) {
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

    //Copies (if needed) and loads the json file in "config/taleofkingdoms/worthiness.jsos"
    private void loadWorthinessJson() {
        File internalFile = new File("worthiness.json");
        File configDirectory = new File("config/" + TaleOfKingdoms.MODID);
        File externalFile = new File(configDirectory, internalFile.getName());

        if(configDirectory.mkdir() || configDirectory.exists()) {
            InputStream fileSrc = Thread.currentThread().getContextClassLoader().getResourceAsStream(internalFile.getPath());

            try {
                if(externalFile.createNewFile()) {
                    Files.copy(fileSrc, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                Reader reader = Files.newBufferedReader(externalFile.toPath());

                worthinessJson = new Gson().fromJson(reader, JsonObject.class);
            } catch (IOException | JsonParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the mob worthiness from the json file
     * @param mob the {@link LivingEntity} killed
     * @return the mob's worthiness if and only if the entry exists, else 1
     */
    public int getMobWorthiness(LivingEntity mob) {
        String mobType = mob.getType().getName().getString();

        if(worthinessJson.has(mobType)) {
            return worthinessJson.get(mobType).getAsInt();
        } else {
            return 1;
        }
    }

    /**
     * Gets the difficulty worthiness from the json file
     * @param world the {@link World} the entity died in
     * @return the difficulty's worthiness if and only if the entry exists, else 1
     */
    public int getDifficultyWorthinessMultiplier(World world) {
        JsonObject difficulty = worthinessJson.getAsJsonObject("difficulty");

        if(difficulty.has(world.getDifficulty().getName())) {
            return difficulty.get(world.getDifficulty().getName()).getAsInt();
        } else {
            return 1;
        }
    }
}
