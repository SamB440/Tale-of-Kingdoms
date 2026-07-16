package com.convallyria.taleofkingdoms.client.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.of(
            (listeners) -> (screen, context, textRenderer) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, context, textRenderer);
                }
            });

    void render(InventoryScreen screen, GuiGraphics context, Font textRenderer);
}
