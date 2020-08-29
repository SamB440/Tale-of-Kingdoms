package net.islandearth.taleofkingdoms.common.event.tok;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.server.network.ServerPlayerEntity;

public interface KingdomStartCallback {

    Event<KingdomStartCallback> EVENT = EventFactory.createArrayBacked(KingdomStartCallback.class,
            (listeners) -> (player, instance) -> {
                for (KingdomStartCallback listener : listeners) {
                    listener.kingdomStart(player, instance);
                }
            });

    void kingdomStart(ServerPlayerEntity player, ConquestInstance instance);
}
