package com.convallyria.taleofkingdoms.common.schematic.blocky.material;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public abstract class BlockDataMaterial {

    private final BlockState state;

    public BlockDataMaterial(BlockState state) {
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }

    public abstract BlockState update(Direction face);
}
