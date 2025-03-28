package com.convallyria.taleofkingdoms.common.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class TOKEntity extends PathAwareEntity implements MultiSkinned {

    protected TOKEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    protected static Identifier identifier(String path) {
        return Identifier.of(TaleOfKingdoms.MODID, path);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickHandSwing();
    }

    /**
     * Applies default entity AI:
     * <br>• {@link SwimGoal}
     */
    protected void applyEntityAI() { this.goalSelector.add(1, new SwimGoal(this)); }

    /**
     * Registers the goals for this entity, along with the defaults specified in {@link #applyEntityAI()}
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(Integer.MAX_VALUE, new LookAroundGoal(this));
        applyEntityAI();
    }

    /**
     * Registers default attributes for this entity:
     * <br>• {@link EntityAttributes#GENERIC_MAX_HEALTH} (20.0D)
     * <br>• {@link EntityAttributes#GENERIC_MOVEMENT_SPEED} (1.0D)
     */
    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D);
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
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() { }

    @Override
    public ItemStack getProjectileType(ItemStack stack) {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public Optional<Identifier> getSkin() {
        return Optional.empty();
    }

    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }
}
