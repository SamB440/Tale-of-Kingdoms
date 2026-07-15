package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class WanderAroundKingdomGoal extends WanderAroundGuildGoal {

    public WanderAroundKingdomGoal(PathfinderMob mob, double speed) { this(mob, speed, 50); }

    public WanderAroundKingdomGoal(PathfinderMob pathAwareEntity, double speed, int chance) {
        this(pathAwareEntity, speed, chance, 30, 7);
    }

    public WanderAroundKingdomGoal(PathfinderMob pathAwareEntity, double speed, int chance, int horizontalRange, int verticalRange) {
        super(pathAwareEntity, speed, chance, horizontalRange, verticalRange);
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
                    boolean isValid = false;
                    for (GuildPlayer guildPlayer : instance.getGuildPlayers().values()) {
                        final PlayerKingdom kingdom = guildPlayer.getKingdom();
                        if (kingdom == null || !kingdom.isInKingdom(blockPos)) continue;
                        isValid = true;
                        break;
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