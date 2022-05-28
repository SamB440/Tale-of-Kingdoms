package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WanderAroundGuildGoal extends Goal {

    protected final PathAwareEntity mob;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected final double speed;
    protected int chance;
    protected boolean ignoringChance;
    private final int horizontalRange, verticalRange;

    public WanderAroundGuildGoal(PathAwareEntity mob, double speed) { this(mob, speed, 50); }

    public WanderAroundGuildGoal(PathAwareEntity pathAwareEntity, double speed, int chance) {
        this(pathAwareEntity, speed, chance, 30, 7);
    }

    public WanderAroundGuildGoal(PathAwareEntity pathAwareEntity, double speed, int chance, int horizontalRange, int verticalRange) {
        this.mob = pathAwareEntity;
        this.speed = speed;
        this.chance = chance;
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
       if (!this.ignoringChance) {
           // if (this.field_24463 && this.mob.getDespawnCounter() >= 100) {
             //   return false;
          //  }

           if (this.mob.getRandom().nextInt(this.chance) != 0) {
               return false;
           }
       }

        Vec3d vec3d = this.getWanderTarget();
        if (vec3d == null) {
            return false;
        } else {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api != null) {
                if (api.getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                    ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
                    BlockPos blockPos = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
                    if (!instance.isInGuild(blockPos)) return false;
                }
            }

            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            this.ignoringChance = false;
            return true;
        }
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        return NoPenaltyTargeting.find(this.mob, horizontalRange, verticalRange);
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && !this.mob.hasPassengers();
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }

    public void ignoreChanceOnce() {
        this.ignoringChance = true;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}