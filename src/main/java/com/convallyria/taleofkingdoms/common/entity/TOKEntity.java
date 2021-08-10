package com.convallyria.taleofkingdoms.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class TOKEntity extends PathfinderMob {

    protected TOKEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
    }

    /**
     * Applies default entity AI:
     * <br>• {@link FloatGoal}
     */
    protected void applyEntityAI() { this.goalSelector.addGoal(1, new FloatGoal(this)); }

    /**
     * Registers the goals for this entity, along with the defaults specified in {@link #applyEntityAI()}
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(Integer.MAX_VALUE, new RandomLookAroundGoal(this));
        applyEntityAI();
    }

    /**
     * Registers default attributes for this entity:
     * <br>• {@link Attributes#MAX_HEALTH} (20.0D)
     * <br>• {@link Attributes#MOVEMENT_SPEED} (1.0D)
     */
    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    /**
     * Whether this entity is intended to be stationary or not.
     * @deprecated pretty useless
     * @return true if intended to be stationary
     */
    @Deprecated
    public abstract boolean isStationary();

    /**
     * Whether this entity is invulnerable.
     *
     * **AI will not function well if this is true!**
     * @return true if invulnerable
     */
    @Override
    public boolean isInvulnerable() {
        return false;
    }

    /**
     * Whether this entity may be pushed by other entities.
     * @return true if entity may be pushed
     */
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() { }
}
