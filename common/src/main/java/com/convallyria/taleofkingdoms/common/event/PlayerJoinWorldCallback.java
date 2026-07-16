package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerJoinWorldCallback {

    Event<PlayerJoinWorldCallback> EVENT = EventFactory.of(
            (listeners) -> (player) -> {
                for (PlayerJoinWorldCallback listener : listeners) {
                    listener.onJoin(player);
                }
            });

    void onJoin(ServerPlayer player);
}
