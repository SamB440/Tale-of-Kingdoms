package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

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
        if (this.pos.isWithinDistance(this.mob.getPos(), this.getDesiredDistanceToTarget())) {
            entity.getGoalSelector().remove(this);
        }
    }

    @Override
    public BlockPos getTargetPos() {
        return this.pos;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return pos.equals(this.pos);
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 0.5;
    }
}