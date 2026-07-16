package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
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
     * @param entity entity to drop coins for
     */
    public static void dropCoins(Entity entity) {
        Consumer<MinecraftServer> dropCoins = server -> dropItem(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN).get(), 1, entity);
        if (isHostileEntity(entity)) {
            TaleOfKingdoms.getAPI().getScheduler().repeatN(dropCoins, 25, 0, 1);
        } else if (entity instanceof IronGolem) {
            TaleOfKingdoms.getAPI().getScheduler().repeatN(dropCoins, 45, 0, 1);
        } else if (entity instanceof Villager villagerEntity) {
            final VillagerProfession profession = villagerEntity.getVillagerData().getProfession();
            if (!profession.name().equals("nitwit") && !profession.name().equals("none")) {
                TaleOfKingdoms.getAPI().getScheduler().repeatN(dropCoins, 10, 0, 1);
            }
        }
    }

    private static void dropItem(Item item, int meta, Entity livingBase) {
        livingBase.spawnAtLocation(item, meta);
    }
}
