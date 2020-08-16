package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.createArrayBacked(InventoryDrawCallback.class,
            (listeners) -> (screen, matrices) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, matrices);
                }
            });

    void render(InventoryScreen screen, MatrixStack matrices);
}
