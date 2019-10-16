package net.islandearth.taleofkingdoms.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class FarmerEntity extends CreatureEntity implements TOKEntity {

	public FarmerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
		//TODO set eye height : 
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 10.0F));
		this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
		applyEntityAI();
	}


	protected void applyEntityAI() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
	}

	@Override
	public boolean isStationary() {
		return true;
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		this.setBoundingBox(this.getBoundingBox().expand(0, 1.5, 0));
		this.recalculateSize();
	}
}
