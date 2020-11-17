package com.convallyria.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerJoinCallback {

    Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class,
            (listeners) -> (connection, player) -> {
                for (PlayerJoinCallback listener : listeners) {
                    listener.onJoin(connection, player);
                }
            });

    void onJoin(ClientConnection connection, ServerPlayerEntity player);
}
