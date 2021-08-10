package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ImprovedFollowTargetGoal<T extends LivingEntity> extends TargetGoal {
    protected final EntityType<T> entityType;
    protected final int reciprocalChance;
    protected LivingEntity targetEntity;
    protected TargetingConditions targetPredicate;

    public ImprovedFollowTargetGoal(Mob mob, EntityType<T> entityType, boolean checkVisibility) {
        this(mob, entityType, checkVisibility, false);
    }

    public ImprovedFollowTargetGoal(Mob mob, EntityType<T> entityType, boolean checkVisibility, boolean checkCanNavigate) {
        this(mob, entityType, 10, checkVisibility, checkCanNavigate, entity -> entity instanceof GuildGuardEntity);
    }

    public ImprovedFollowTargetGoal(Mob mob, EntityType<T> entityType, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, checkVisibility, checkCanNavigate);
        this.entityType = entityType;
        this.reciprocalChance = reciprocalChance;
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.targetPredicate = (TargetingConditions.forCombat()).range(this.getFollowDistance()).selector(targetPredicate);
    }

    @Override
    public boolean canUse() {
        if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        } else {
            this.findClosestTarget();
            return this.targetEntity != null;
        }
    }

    protected AABB getSearchBox(double distance) {
        return this.mob.getBoundingBox().inflate(distance, 4.0D, distance);
    }

    protected void findClosestTarget() {
        if (this.entityType != EntityType.PLAYER) {
            AABB box = this.getSearchBox(this.getFollowDistance());
            List<T> entities = this.mob.level.getEntities(entityType, box, entity -> {
                if (mustSee) return mob.hasLineOfSight(entity);
                return true;
            });
            LivingEntity current = null;
            for (T entity : entities) {
                if (current == null) {
                    current = entity;
                    continue;
                }

                if (entity.distanceToSqr(mob) < current.distanceToSqr(mob)) {
                    current = entity;
                }
            }
            this.targetEntity = current;
        } else {
            this.targetEntity = this.mob.level.getNearestPlayer(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.getFollowDistance(), true);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    public void setTargetEntity(@Nullable LivingEntity targetEntity) {
        this.targetEntity = targetEntity;
    }
}
