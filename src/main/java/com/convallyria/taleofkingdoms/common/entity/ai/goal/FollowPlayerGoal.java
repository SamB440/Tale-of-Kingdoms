package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FollowPlayerGoal extends Goal {
    private final MobEntity mob;
    private final Predicate<PlayerEntity> targetPredicate;
    private LivingEntity target;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float minDistance;
    private float oldWaterPathFindingPenalty;
    private final float maxDistance;

    public FollowPlayerGoal(MobEntity mob, double speed, float minDistance, float maxDistance) {
        this.mob = mob;
        this.targetPredicate = Objects::nonNull;
        this.speed = speed;
        this.navigation = mob.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        if (!(mob.getNavigation() instanceof MobNavigation) && !(mob.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    @Override
    public boolean canStart() {
        List<PlayerEntity> list = this.mob.world.getEntitiesByClass(PlayerEntity.class, this.mob.getBoundingBox().expand(this.maxDistance), this.targetPredicate);
        if (!list.isEmpty()) {

            for (PlayerEntity playerEntity : list) {
                if (!playerEntity.isInvisible()) {
                    this.target = playerEntity;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.target != null && !this.navigation.isIdle() && this.mob.squaredDistanceTo(this.target) > (double)(this.minDistance * this.minDistance);
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathFindingPenalty = this.mob.getPathfindingPenalty(PathNodeType.WATER);
        this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.target = null;
        this.navigation.stop();
        this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathFindingPenalty);
    }

    @Override
    public void tick() {
        if (this.target != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().lookAt(this.target, 10.0F, (float)this.mob.getLookPitchSpeed());
            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = 10;
                double d = this.mob.getX() - this.target.getX();
                double e = this.mob.getY() - this.target.getY();
                double f = this.mob.getZ() - this.target.getZ();
                double g = d * d + e * e + f * f;
                if (g > (double)(this.minDistance * this.minDistance)) {
                    this.navigation.startMovingTo(this.target, this.speed);
                    if (g > (maxDistance * maxDistance)) {
                        this.mob.teleport(target.getX(), target.getY(), target.getZ(), true);
                    }
                } else {
                    this.navigation.stop();
                    if (g <= (double)this.minDistance || target.getX() == this.mob.getX() && target.getY() == this.mob.getY() && target.getZ() == this.mob.getZ()) {
                        double h = this.target.getX() - this.mob.getX();
                        double i = this.target.getZ() - this.mob.getZ();
                        this.navigation.startMovingTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speed);
                    }
                }
            }
        }
    }
}
