package net.islandearth.taleofkingdoms.common.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NPCEntity extends PlayerEntity {
	
	protected NPCEntity(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
		this.setUniqueId(PlayerEntity.getUUID(gameProfileIn));
		BlockPos blockpos = worldIn.getSpawnPoint();
		this.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)(blockpos.getY() + 1), (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
		this.unused180 = 180.0F;
	 }

	@Override
	public HandSide getPrimaryHand() {
		return HandSide.RIGHT;
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return true;
	}
}
