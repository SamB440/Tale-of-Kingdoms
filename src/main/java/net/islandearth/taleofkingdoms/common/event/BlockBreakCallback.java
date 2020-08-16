package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface BlockBreakCallback {

    Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(BlockBreakCallback.class,
            (listeners) -> (player, block, pos) -> {
                for (BlockBreakCallback listener : listeners) {
                    ActionResult result = listener.interact(player, block, pos);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, Block block, BlockPos pos);
}
