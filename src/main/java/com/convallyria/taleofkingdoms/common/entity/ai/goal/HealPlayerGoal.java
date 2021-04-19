package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class HealPlayerGoal extends Goal {

    private final MobEntity mob;
    private LivingEntity target;
    private final float maxDistance;

    public HealPlayerGoal(MobEntity mob, float maxDistance) {
        this.mob = mob;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean canStart() {
        List<PlayerEntity> list = this.mob.world.getEntitiesByClass(PlayerEntity.class, this.mob.getBoundingBox().expand(this.maxDistance), PlayerEntity::isAlive);
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
        return this.target != null &&  this.mob.squaredDistanceTo(this.target) < (double)(this.maxDistance * this.maxDistance);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 20, 1);
            target.addStatusEffect(statusEffectInstance);
        }
    }
}
