package com.convallyria.taleofkingdoms.common.schematic.blocky.material;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.Direction;

public class DirectionalBlockDataMaterial extends BlockDataMaterial {

    public DirectionalBlockDataMaterial(BlockState state) {
        super(state);
    }

    @Override
    public BlockState update(Direction face) {
        BlockState state = this.getState();
        FacingBlock facing = (FacingBlock) state.getBlock();
        Direction currentFace = state.get(FacingBlock.FACING);
        switch (face) {
            case NORTH:
                switch (currentFace) {
                    case NORTH:
                        state.with(FacingBlock.FACING, Direction.NORTH);
                        break;
                    case SOUTH:
                        state.with(FacingBlock.FACING, Direction.SOUTH);
                        break;
                    case EAST:
                        state.with(FacingBlock.FACING, Direction.WEST);
                        break;
                    case WEST:
                        state.with(FacingBlock.FACING, Direction.EAST);
                        break;
                    default:
                        break;
                }

                break;
            case EAST:
                switch (currentFace) {
                    case NORTH:
                        state.with(FacingBlock.FACING, Direction.EAST);
                        break;
                    case SOUTH:
                        state.with(FacingBlock.FACING, Direction.WEST);
                        break;
                    case EAST:
                        state.with(FacingBlock.FACING, Direction.NORTH);
                        break;
                    case WEST:
                        state.with(FacingBlock.FACING, Direction.SOUTH);
                        break;
                    default:
                        break;
                }

                break;
            case SOUTH:
                switch (currentFace) {
                    case NORTH:
                        state.with(FacingBlock.FACING, Direction.SOUTH);
                        break;
                    case SOUTH:
                        state.with(FacingBlock.FACING, Direction.NORTH);
                        break;
                    case EAST:
                        state.with(FacingBlock.FACING, Direction.EAST);
                        break;
                    case WEST:
                        state.with(FacingBlock.FACING, Direction.WEST);
                        break;
                    default:
                        break;
                }

                break;
            case WEST:
                switch (currentFace) {
                    case NORTH:
                        state.with(FacingBlock.FACING, Direction.WEST);
                        break;
                    case SOUTH:
                        state.with(FacingBlock.FACING, Direction.EAST);
                        break;
                    case EAST:
                        state.with(FacingBlock.FACING, Direction.SOUTH);
                        break;
                    case WEST:
                        state.with(FacingBlock.FACING, Direction.NORTH);
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
        return state;
    }
}
