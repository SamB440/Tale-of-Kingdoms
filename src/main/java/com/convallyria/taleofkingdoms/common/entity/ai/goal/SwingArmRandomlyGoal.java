package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;

import java.util.concurrent.ThreadLocalRandom;

public class SwingArmRandomlyGoal extends Goal {

    private final MobEntity mob;

    public SwingArmRandomlyGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @Override
    public boolean shouldContinue() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @Override
    public void tick() {
        mob.swingHand(Hand.MAIN_HAND);
    }
}
