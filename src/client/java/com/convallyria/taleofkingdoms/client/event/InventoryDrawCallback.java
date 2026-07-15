package com.convallyria.taleofkingdoms.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.createArrayBacked(InventoryDrawCallback.class,
            (listeners) -> (screen, context, textRenderer) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, context, textRenderer);
                }
            });

    void render(InventoryScreen screen, GuiGraphics context, Font textRenderer);
}
