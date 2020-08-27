package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.Block;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface BlockBreakCallback {

    Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(BlockBreakCallback.class,
            (listeners) -> (block, pos) -> {
                for (BlockBreakCallback listener : listeners) {
                    ActionResult result = listener.blockBreak(block, pos);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult blockBreak(Block block, BlockPos pos);
}
