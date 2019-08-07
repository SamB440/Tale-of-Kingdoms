package net.islandearth.taleofkingdoms.common.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EntityFarmer extends NPCEntity {

	public EntityFarmer(World worldIn) {
		super(worldIn, new GameProfile(UUID.randomUUID(), "Farmer"));
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(new HoeItem(ItemTier.IRON, 0, new Item.Properties())));
	}

}
