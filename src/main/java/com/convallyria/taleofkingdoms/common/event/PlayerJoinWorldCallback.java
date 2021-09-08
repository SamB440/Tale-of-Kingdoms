package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerJoinWorldCallback {

    Event<PlayerJoinWorldCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinWorldCallback.class,
            (listeners) -> (player) -> {
                for (PlayerJoinWorldCallback listener : listeners) {
                    listener.onJoin(player);
                }
            });

    void onJoin(ServerPlayerEntity player);
}
