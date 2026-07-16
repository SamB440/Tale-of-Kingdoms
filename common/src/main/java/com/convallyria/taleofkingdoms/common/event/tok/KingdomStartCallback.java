package com.convallyria.taleofkingdoms.common.event.tok;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface KingdomStartCallback {

    Event<KingdomStartCallback> EVENT = EventFactory.of(
            (listeners) -> (player, instance) -> {
                for (KingdomStartCallback listener : listeners) {
                    listener.kingdomStart(player, instance);
                }
            });

    void kingdomStart(ServerPlayer player, ConquestInstance instance);
}
