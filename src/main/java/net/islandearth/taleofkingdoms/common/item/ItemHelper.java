package net.islandearth.taleofkingdoms.common.item;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;

public class ItemHelper {
	
	private static Random random = new Random();
	public static boolean isHostileEntity(EntityLivingBase entityLiving) { return (entityLiving instanceof net.minecraft.entity.monster.EntityBlaze || entityLiving instanceof net.minecraft.entity.monster.EntityCaveSpider || entityLiving instanceof net.minecraft.entity.monster.EntityCreeper || entityLiving instanceof net.minecraft.entity.boss.EntityDragon || entityLiving instanceof net.minecraft.entity.monster.EntityEnderman || entityLiving instanceof net.minecraft.entity.monster.EntityGhast || entityLiving instanceof net.minecraft.entity.monster.EntityMagmaCube || entityLiving instanceof net.minecraft.entity.monster.EntityPigZombie || entityLiving instanceof net.minecraft.entity.monster.EntitySilverfish || entityLiving instanceof net.minecraft.entity.monster.EntitySkeleton || entityLiving instanceof net.minecraft.entity.monster.EntitySpider || entityLiving instanceof net.minecraft.entity.monster.EntityWitch || entityLiving instanceof net.minecraft.entity.boss.EntityWither || entityLiving instanceof net.minecraft.entity.monster.EntityZombie); }


	public static void dropCoins(EntityLivingBase entityLiving) {
		if (isHostileEntity(entityLiving)) {
	      
	      int bound = random.nextInt(25);
	      
	      for (int i = 0; i < bound; i++) {
	        
	        dropItem(ItemRegistry.items.get("coin"), 1, entityLiving);
	      } 
	    } 
	  }
	private static void dropItem(Item item, int meta, EntityLivingBase livingBase) { livingBase.dropItem(item, meta); }
}
