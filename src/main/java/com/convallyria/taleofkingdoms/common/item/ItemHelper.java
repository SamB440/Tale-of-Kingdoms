package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public class ItemHelper {

    /**
     * Checks if an entity is hostile.
     * @param entityLiving entity to check
     * @return true if entity is hostile
     */
    public static boolean isHostileEntity(Entity entityLiving) {
        return entityLiving instanceof Monster;
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
            TaleOfKingdoms.getAPI().getScheduler().repeatN(dropCoins, 25, 0, 1);
        }
    }

    private static void dropItem(Item item, int meta, LivingEntity livingBase) {
        livingBase.dropItem(item, meta);
    }
}
