package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class HealPlayerGoal extends Goal {

    private final Mob mob;
    private LivingEntity target;
    private final float maxDistance;

    public HealPlayerGoal(Mob mob, float maxDistance) {
        this.mob = mob;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean canUse() {
        List<Player> list = this.mob.level.getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.maxDistance), Player::isAlive);
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
        return this.target != null &&  this.mob.distanceToSqr(this.target) < (double)(this.maxDistance * this.maxDistance);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            MobEffectInstance statusEffectInstance = new MobEffectInstance(MobEffects.REGENERATION, 20, 1);
            target.addEffect(statusEffectInstance);
        }
    }
}
