package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import com.convallyria.taleofkingdoms.common.event.ItemMergeCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
     * and the player gets worthiness added equal to the {@link CoinListener#getMobWorthiness(Entity)} times (*) the {@link CoinListener#getDifficultyWorthinessMultiplier(Level)}
     */
    private void dropCoinsOnDeath() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                Player playerEntity = null;
                if (entity instanceof Player) {
                    final GuildPlayer guildPlayer = instance.getPlayer(entity.getUUID());
                    int subtract = (guildPlayer.getCoins() / 20);
                    guildPlayer.setCoins(guildPlayer.getCoins() - subtract);
                    return;
                }

                if (entity instanceof HunterEntity) {
                    for (UUID playerUuid : instance.getGuildPlayers().keySet()) {
                        final GuildPlayer loopPlayer = instance.getPlayer(playerUuid);
                        final List<UUID> hunterUuids = loopPlayer.getHunters();
                        hunterUuids.remove(entity.getUUID());
                        TaleOfKingdoms.LOGGER.info("Hunter {} died and was removed", entity.getUUID());
                    }
                    return;
                }

                if (source.getDirectEntity() instanceof Player
                        || source.getDirectEntity() instanceof HunterEntity
                        || source.getDirectEntity() instanceof GuildGuardEntity
                        || source.getDirectEntity() instanceof Projectile) {
                    if (source.getDirectEntity() instanceof Projectile projectileEntity) {
                        if (projectileEntity.getOwner() instanceof Player) {
                            playerEntity = (Player) projectileEntity.getOwner();
                        }

                        if (!(projectileEntity.getOwner() instanceof Player)
                                && !(projectileEntity.getOwner() instanceof HunterEntity)) {
                            return;
                        }
                    } else if (source.getDirectEntity() instanceof Player) {
                        playerEntity = (Player) source.getDirectEntity();
                    }

                    //TODO associate owner with hunter entity
                    ItemHelper.dropCoins(entity);

                    if (source.getDirectEntity() instanceof Player) {
                        final GuildPlayer guildPlayer = instance.getPlayer(source.getDirectEntity().getUUID());
                        guildPlayer.setWorthiness(guildPlayer.getWorthiness() + (getMobWorthiness(entity) * getDifficultyWorthinessMultiplier(source.getDirectEntity().level())));
                    }

                    if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
                        instance.attack(serverPlayerEntity, serverPlayerEntity.serverLevel());
                        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER)
                            ServerConquestInstance.sync(serverPlayerEntity, instance);
                    }
                }
            });
        });
    }

    private void coinPickup() {
        EntityPickupItemCallback.EVENT.register((player, item) -> {
            if (equalsCoin(item)) {
                TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                    Random random = ThreadLocalRandom.current();
                    instance.addCoins(player.getUUID(), random.nextInt(5));
                    if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
                        ServerConquestInstance.sync((ServerPlayer) player, instance);
                    }
                });

                player.getInventory().clearOrCountMatchingItems(predicate -> predicate.getItem().equals(item.getItem()), -1, player.getInventory());
            }
        });
    }

    private void preventCoinMerge() {
        ItemMergeCallback.EVENT.register((stack1, stack2) -> !equalsCoin(stack1) || !equalsCoin(stack2));
    }

    private boolean equalsCoin(ItemStack stack) {
        return stack.getItem().equals(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN));
    }

    //Copies (if needed) and loads the json file in "config/taleofkingdoms/worthiness.json"
    private void loadWorthinessJson() {
        File internalFile = new File("worthiness.json");
        File configDirectory = new File("config/" + TaleOfKingdoms.MODID);
        File externalFile = new File(configDirectory, internalFile.getName());

        if (configDirectory.mkdir() || configDirectory.exists()) {
            InputStream fileSrc = Thread.currentThread().getContextClassLoader().getResourceAsStream(internalFile.getPath());

            try {
                if (externalFile.createNewFile()) {
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
    public int getMobWorthiness(Entity mob) {
        String mobType = mob.getType().getDescription().getString();

        if(worthinessJson.has(mobType)) {
            return worthinessJson.get(mobType).getAsInt();
        } else {
            return 1;
        }
    }

    /**
     * Gets the difficulty worthiness from the json file
     * @param world the {@link Level} the entity died in
     * @return the difficulty's worthiness if and only if the entry exists, else 1
     */
    public int getDifficultyWorthinessMultiplier(Level world) {
        JsonObject difficulty = worthinessJson.getAsJsonObject("difficulty");

        if(difficulty.has(world.getDifficulty().getKey())) {
            return difficulty.get(world.getDifficulty().getKey()).getAsInt();
        } else {
            return 1;
        }
    }
}
