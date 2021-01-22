package com.convallyria.taleofkingdoms.common.event.tok;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;

public interface BlockPlaceCallback {

    Event<BlockPlaceCallback> EVENT = EventFactory.createArrayBacked(BlockPlaceCallback.class,
            (listeners) -> (block, player) -> {
                for (BlockPlaceCallback listener : listeners) {
                    listener.blockPlace(block, player);
                }
            });

    void blockPlace(BlockState block, PlayerEntity player);
}
