package net.islandearth.taleofkingdoms.client.entity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class FarmerEntity extends CreatureEntity implements TOKEntity {
	
	// From PlayerEntity (net.minecraft)
	private static final EntitySize STANDING_SIZE = EntitySize.flexible(0.6F, 1.8F);
	private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.SLEEPING, SLEEPING_SIZE).put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SNEAKING, EntitySize.flexible(0.6F, 1.5F)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();
	
	public FarmerEntity(World worldIn) {
		super(EntityTypes.FARMER, worldIn);
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
	}
	
	public FarmerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 10.0F));
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
	public boolean isInvulnerable() {
		return true;
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
	}
	
	@Override
	public EntitySize getSize(Pose poseIn) {
		return SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
	}
}
