package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class WalkToTargetGoal extends MoveToBlockGoal {

    private final PathfinderMob entity;
    private final BlockPos pos;

    public WalkToTargetGoal(PathfinderMob entity, double speed, BlockPos pos) {
        super(entity, speed, 100, entity.level().getHeight());
        this.entity = entity;
        this.pos = pos;
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