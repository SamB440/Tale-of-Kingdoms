package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WanderAroundKingdomGoal extends WanderAroundGuildGoal {

    public WanderAroundKingdomGoal(PathAwareEntity mob, double speed) { this(mob, speed, 50); }

    public WanderAroundKingdomGoal(PathAwareEntity pathAwareEntity, double speed, int chance) {
        this(pathAwareEntity, speed, chance, 30, 7);
    }

    public WanderAroundKingdomGoal(PathAwareEntity pathAwareEntity, double speed, int chance, int horizontalRange, int verticalRange) {
        super(pathAwareEntity, speed, chance, horizontalRange, verticalRange);
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
                    BlockPos blockPos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
                    boolean isValid = false;
                    for (PlayerKingdom kingdom : instance.getKingdoms()) {
                        if (kingdom.isInKingdom(blockPos)) isValid = true;
                    }

                    if (!isValid) return false;
                }
            }

            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            this.ignoringChance = false;
            return true;
        }
    }
}