package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;

public interface BlockBreakCallback {

    Event<BlockBreakCallback> EVENT = EventFactory.of(
            (listeners) -> (block, pos) -> {
                for (BlockBreakCallback listener : listeners) {
                    InteractionResult result = listener.blockBreak(block, pos);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult blockBreak(Block block, BlockPos pos);
}
