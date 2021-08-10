package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.MovementVaried;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FollowPlayerGoal extends Goal {
    private final Mob mob;
    private final Predicate<Player> targetPredicate;
    private LivingEntity target;
    private final double speed;
    private final PathNavigation navigation;
    private int updateCountdownTicks;
    private final float minDistance;
    private float oldWaterPathFindingPenalty;
    private final float maxDistance;

    public FollowPlayerGoal(Mob mob, double speed, float minDistance, float maxDistance) {
        this.mob = mob;
        this.targetPredicate = Objects::nonNull;
        this.speed = speed;
        this.navigation = mob.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof MovementVaried movementVaried) {
            if (!movementVaried.isMovementEnabled()) return false;
        }

        List<Player> list = this.mob.level.getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.maxDistance), this.targetPredicate);
        if (!list.isEmpty()) {
            for (Player playerEntity : list) {
                if (!playerEntity.isInvisible()) {
                    this.target = playerEntity;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        boolean flag = true;
        if (this.mob instanceof MovementVaried movementVaried) {
            if (!movementVaried.isMovementEnabled()) flag = false;
        }
        return flag && this.target != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.target) > (double)(this.minDistance * this.minDistance);
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathFindingPenalty = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.target = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterPathFindingPenalty);
    }

    @Override
    public void tick() {
        if (this.target != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.target, 10.0F, (float)this.mob.getMaxHeadXRot());
            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = 10;
                double d = this.mob.getX() - this.target.getX();
                double e = this.mob.getY() - this.target.getY();
                double f = this.mob.getZ() - this.target.getZ();
                double g = d * d + e * e + f * f;
                if (g > (double)(this.minDistance * this.minDistance)) {
                    this.navigation.moveTo(this.target, this.speed);
                    if (g > (maxDistance * maxDistance)) {
                        this.mob.randomTeleport(target.getX(), target.getY(), target.getZ(), true);
                    }
                } else {
                    this.navigation.stop();
                    if (g <= (double)this.minDistance || target.getX() == this.mob.getX() && target.getY() == this.mob.getY() && target.getZ() == this.mob.getZ()) {
                        double h = this.target.getX() - this.mob.getX();
                        double i = this.target.getZ() - this.mob.getZ();
                        this.navigation.moveTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speed);
                    }
                }
            }
        }
    }
}
