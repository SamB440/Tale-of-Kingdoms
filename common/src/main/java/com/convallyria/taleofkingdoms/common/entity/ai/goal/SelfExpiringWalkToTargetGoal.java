package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class SelfExpiringWalkToTargetGoal extends WalkToTargetGoal {

    private final TOKEntity entity;
    private final BlockPos pos;

    public SelfExpiringWalkToTargetGoal(TOKEntity entity, double speed, BlockPos pos) {
        super(entity, speed, pos);
        this.entity = entity;
        this.pos = pos;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.pos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
            entity.getGoalSelector().removeGoal(this);
        }
    }

    @Override
    public BlockPos getMoveToTarget() {
        return this.pos;
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        return pos.equals(this.pos);
    }

    @Override
    public double acceptedDistance() {
        return 0.5;
    }
}