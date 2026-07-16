package com.convallyria.taleofkingdoms.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerLeaveCallback {

    Event<PlayerLeaveCallback> EVENT = EventFactory.of(
            (listeners) -> (player) -> {
                for (PlayerLeaveCallback listener : listeners) {
                    listener.onLeave(player);
                }
            });

    void onLeave(ServerPlayer player);
}
