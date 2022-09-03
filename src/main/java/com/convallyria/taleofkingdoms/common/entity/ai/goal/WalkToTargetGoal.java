package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class WalkToTargetGoal extends MoveToTargetPosGoal {

    private final PathAwareEntity entity;
    private final BlockPos pos;

    public WalkToTargetGoal(PathAwareEntity entity, double speed, BlockPos pos) {
        super(entity, speed, Integer.MAX_VALUE);
        this.entity = entity;
        this.pos = pos;
    }

    @Override
    public boolean canStart() {
        return super.canStart();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return pos.equals(this.pos);
    }
}