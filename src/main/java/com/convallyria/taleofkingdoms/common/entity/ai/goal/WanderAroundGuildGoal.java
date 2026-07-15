package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class WanderAroundGuildGoal extends Goal {

    protected final PathfinderMob mob;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected final double speed;
    protected int chance;
    protected boolean ignoringChance;
    private final int horizontalRange, verticalRange;

    public WanderAroundGuildGoal(PathfinderMob mob, double speed) { this(mob, speed, 50); }

    public WanderAroundGuildGoal(PathfinderMob pathAwareEntity, double speed, int chance) {
        this(pathAwareEntity, speed, chance, 30, 7);
    }

    public WanderAroundGuildGoal(PathfinderMob pathAwareEntity, double speed, int chance, int horizontalRange, int verticalRange) {
        this.mob = pathAwareEntity;
        this.speed = speed;
        this.chance = chance;
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
       if (!this.ignoringChance) {
           // if (this.field_24463 && this.mob.getDespawnCounter() >= 100) {
             //   return false;
          //  }

           if (this.mob.getRandom().nextInt(this.chance) != 0) {
               return false;
           }
       }

        Vec3 vec3d = this.getWanderTarget();
        if (vec3d == null) {
            return false;
        } else {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api != null) {
                if (api.getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                    ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
                    BlockPos blockPos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
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
    protected Vec3 getWanderTarget() {
        return DefaultRandomPos.getPos(this.mob, horizontalRange, verticalRange);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && !this.mob.isVehicle();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
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