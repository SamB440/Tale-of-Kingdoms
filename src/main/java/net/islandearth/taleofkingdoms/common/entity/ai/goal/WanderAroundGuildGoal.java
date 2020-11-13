package net.islandearth.taleofkingdoms.common.entity.ai.goal;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.ai.TargetFinder;
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
    private boolean field_24463;

    public WanderAroundGuildGoal(PathAwareEntity mob, double speed) { this(mob, speed, 50); }

    public WanderAroundGuildGoal(PathAwareEntity mob, double speed, int chance) {
        this(mob, speed, chance, true);
    }

    public WanderAroundGuildGoal(PathAwareEntity pathAwareEntity, double d, int i, boolean bl) {
        this.mob = pathAwareEntity;
        this.speed = d;
        this.chance = i;
        this.field_24463 = bl;
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
            if (TaleOfKingdoms.getAPI().isPresent()) {
                if (TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                    ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
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
        return TargetFinder.findTarget(this.mob, 30, 7);
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