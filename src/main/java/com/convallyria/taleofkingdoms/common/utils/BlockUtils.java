package com.convallyria.taleofkingdoms.common.utils;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    @Nullable
    public static BlockPos locateRestingPlace(ConquestInstance instance, PlayerEntity player) {
        List<BlockPos> validRest = instance.getSleepLocations(player);

        if (validRest.isEmpty()) return null;
        Random rand = ThreadLocalRandom.current();
        return validRest.get(rand.nextInt(validRest.size()));
    }
}
