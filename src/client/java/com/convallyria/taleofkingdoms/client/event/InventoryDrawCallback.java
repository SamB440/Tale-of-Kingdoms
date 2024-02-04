package com.convallyria.taleofkingdoms.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.createArrayBacked(InventoryDrawCallback.class,
            (listeners) -> (screen, context, textRenderer) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, context, textRenderer);
                }
            });

    void render(InventoryScreen screen, DrawContext context, TextRenderer textRenderer);
}
