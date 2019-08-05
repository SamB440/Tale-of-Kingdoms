package net.islandearth.taleofkingdoms.common.item;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;

public class ItemHelper {
	
	private static Random random = new Random();
	
	public static boolean isHostileEntity(EntityLivingBase entityLiving) { 
		return entityLiving.isCreatureType(EnumCreatureType.MONSTER, false);
	}
	
	public static void dropCoins(EntityLivingBase entityLiving) {
		if (isHostileEntity(entityLiving) && !entityLiving.world.isRemote) {
			int bound = random.nextInt(25);
			for (int i = 0; i < bound; i++) {
				dropItem(ItemRegistry.items.get("coin"), 1, entityLiving);
			} 
		} 
	}
	
	private static void dropItem(Item item, int meta, EntityLivingBase livingBase) { 
		livingBase.entityDropItem(item, meta); 
	}
}
