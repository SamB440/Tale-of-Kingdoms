package net.islandearth.taleofkingdoms.common.item;

import java.util.Random;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;

public class ItemHelper {
	
	private static final Random random = new Random();
	
	public static boolean isHostileEntity(LivingEntity entityLiving) { 
		return entityLiving instanceof MonsterEntity;
	}
	
	public static void dropCoins(LivingEntity entityLiving) {
		if (isHostileEntity(entityLiving) && !entityLiving.world.isRemote) {
			int bound = random.nextInt(25);
			for (int i = 0; i < bound; i++) {
				dropItem(ItemRegistry.items.get("coin"), 1, entityLiving);
			} 
		} 
	}
	
	private static void dropItem(Item item, int meta, LivingEntity livingBase) { 
		livingBase.entityDropItem(item, meta); 
	}
}
