package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerJoinCallback {

    Event<PlayerJoinCallback> EVENT = EventFactory.of(
            (listeners) -> (connection, player) -> {
                for (PlayerJoinCallback listener : listeners) {
                    listener.onJoin(connection, player);
                }
            });

    void onJoin(Connection connection, ServerPlayer player);
}
