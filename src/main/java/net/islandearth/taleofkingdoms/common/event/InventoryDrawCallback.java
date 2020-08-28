package net.islandearth.taleofkingdoms.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.createArrayBacked(InventoryDrawCallback.class,
            (listeners) -> (screen, matrices, textRenderer) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, matrices, textRenderer);
                }
            });

    void render(InventoryScreen screen, MatrixStack matrices, TextRenderer textRenderer);
}
