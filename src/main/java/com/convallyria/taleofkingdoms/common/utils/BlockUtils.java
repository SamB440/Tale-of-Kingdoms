package com.convallyria.taleofkingdoms.common.utils;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

    public static List<BlockPos> getNearbyBlocks(BlockPos location, int radius) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for (int y = location.getY() - radius; y <= location.getY() + radius; y++) {
                for (int z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<BlockPos> getNearbyBlocksUnder(BlockPos location, int radius) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int y = location.getY() - radius; y == location.getY() - radius; y++) {
            for (int x = location.getX() - radius; x <= location.getX() + radius; x++) {
                for (int z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }
}
