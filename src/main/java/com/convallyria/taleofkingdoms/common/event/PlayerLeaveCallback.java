package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerLeaveCallback {

    Event<PlayerLeaveCallback> EVENT = EventFactory.createArrayBacked(PlayerLeaveCallback.class,
            (listeners) -> (player) -> {
                for (PlayerLeaveCallback listener : listeners) {
                    listener.onLeave(player);
                }
            });

    void onLeave(ServerPlayerEntity player);
}
