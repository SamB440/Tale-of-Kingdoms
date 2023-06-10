package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class WalkToTargetGoal extends MoveToTargetPosGoal {

    private final PathAwareEntity entity;
    private final BlockPos pos;

    public WalkToTargetGoal(PathAwareEntity entity, double speed, BlockPos pos) {
        super(entity, speed, 100, entity.getWorld().getHeight());
        this.entity = entity;
        this.pos = pos;
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