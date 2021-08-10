package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ItemHelper {

    /**
     * Checks if an entity is hostile.
     * @param entityLiving entity to check
     * @return true if entity is hostile
     */
    public static boolean isHostileEntity(Entity entityLiving) {
        return entityLiving instanceof Enemy;
    }

    /**
     * Drops coins for an entity if it is hostile.
     * This **no longer** uses the same randomness used in the old TOK mod.
     * @see ItemCoin
     * @param entityLiving entity to drop coins for
     */
    public static void dropCoins(LivingEntity entityLiving) {
        if (isHostileEntity(entityLiving)) {
            Consumer<MinecraftServer> dropCoins = server -> dropItem(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN), 1, entityLiving);
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getScheduler().repeatN(dropCoins, 25, 0, 1);
            });
        }
    }

    private static void dropItem(Item item, int meta, LivingEntity livingBase) {
        livingBase.spawnAtLocation(item, meta);
    }
}
