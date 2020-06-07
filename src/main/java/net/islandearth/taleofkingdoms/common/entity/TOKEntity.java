package net.islandearth.taleofkingdoms.common.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
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
			.put(Pose.CROUCHING, EntitySize.flexible(0.6F, 1.5F))
			.put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();

	protected TOKEntity(World worldIn) {
		super(null, worldIn);
	}

	protected TOKEntity(EntityType<? extends TOKEntity> entityType, World world) {
		super(entityType, world);
	}

	/**
	 * Applies default entity AI:
	 * <br>• {@link SwimGoal}
	 */
	protected void applyEntityAI() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return SIZE_BY_POSE.getOrDefault(poseIn, STANDING_SIZE);
	}

	/**
	 * Registers the goals for this entity, along with the defaults specified in {@link #applyEntityAI()}
	 */
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(Integer.MAX_VALUE, new LookRandomlyGoal(this));
		applyEntityAI();
	}

	/**
	 * Registers default attributes for this entity:
	 * <br>• {@link SharedMonsterAttributes#MAX_HEALTH} (20.0D)
	 * <br>• {@link SharedMonsterAttributes#MOVEMENT_SPEED} (1.0D)
	 */
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
	}

	/**
	 * Whether this entity is intended to be stationary or not.
	 * @return true if intended to be stationary
	 */
	public abstract boolean isStationary();

	/**
	 * Whether this entity is invulnerable.
	 * @return true if invulnerable
	 */
	@Override
	public boolean isInvulnerable() {
		return true;
	}

	/**
	 * Whether this entity may be pushed by other entities.
	 * @return true if entity may be pushed
	 */
	@Override
	public boolean canBePushed() {
		return false;
	}
}
