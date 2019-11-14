package net.islandearth.taleofkingdoms.client.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.world.World;

import java.util.Map;

public abstract class TOKEntity extends CreatureEntity {

	// From PlayerEntity (net.minecraft)
	private static final EntitySize STANDING_SIZE = EntitySize.flexible(0.6F, 1.8F);
	private static final Map<Pose, EntitySize> SIZE_BY_POSE = ImmutableMap
			.<Pose, EntitySize>builder()
			.put(Pose.STANDING, STANDING_SIZE)
			.put(Pose.SLEEPING, SLEEPING_SIZE)
			.put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F))
			.put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F))
			.put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F))
			.put(Pose.SNEAKING, EntitySize.flexible(0.6F, 1.5F))
			.put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F))
			.build();

	protected TOKEntity(World worldIn) {
		super(null, worldIn);
	}

	protected TOKEntity(EntityType<? extends TOKEntity> entityType, World world) {
		super(entityType, world);
	}

	void applyEntityAI() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
	}

	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
		applyEntityAI();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
	}

	public abstract boolean isStationary();

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}
}
