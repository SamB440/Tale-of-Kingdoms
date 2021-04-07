package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ImprovedFollowTargetGoal<T extends LivingEntity> extends TrackTargetGoal {
    protected final EntityType<T> entityType;
    protected final int reciprocalChance;
    protected LivingEntity targetEntity;
    protected TargetPredicate targetPredicate;

    public ImprovedFollowTargetGoal(MobEntity mob, EntityType<T> entityType, boolean checkVisibility) {
        this(mob, entityType, checkVisibility, false);
    }

    public ImprovedFollowTargetGoal(MobEntity mob, EntityType<T> entityType, boolean checkVisibility, boolean checkCanNavigate) {
        this(mob, entityType, 10, checkVisibility, checkCanNavigate, entity -> entity instanceof GuildGuardEntity);
    }

    public ImprovedFollowTargetGoal(MobEntity mob, EntityType<T> entityType, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, checkVisibility, checkCanNavigate);
        this.entityType = entityType;
        this.reciprocalChance = reciprocalChance;
        this.setControls(EnumSet.of(Control.TARGET));
        this.targetPredicate = (new TargetPredicate()).setBaseMaxDistance(this.getFollowRange()).setPredicate(targetPredicate);
    }

    @Override
    public boolean canStart() {
        if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        } else {
            this.findClosestTarget();
            return this.targetEntity != null;
        }
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0D, distance);
    }

    protected void findClosestTarget() {
        if (this.entityType != EntityType.PLAYER) {
            Box box = this.getSearchBox(this.getFollowRange());
            List<T> entities = this.mob.world.getEntitiesByType(entityType, box, entity -> {
                if (checkVisibility) return mob.canSee(entity);
                else return true;
            });
            LivingEntity current = null;
            for (T entity : entities) {
                if (current == null) {
                    current = entity;
                    continue;
                }

                if (entity.squaredDistanceTo(mob) < current.squaredDistanceTo(mob)) {
                    current = entity;
                }
            }
            this.targetEntity = current;
        } else {
            this.targetEntity = this.mob.world.getClosestPlayer(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.getFollowRange(), true);
            System.out.println(targetEntity);
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
