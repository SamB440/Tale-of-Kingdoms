package com.convallyria.taleofkingdoms.common.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public interface InventoryDrawCallback {

    Event<InventoryDrawCallback> EVENT = EventFactory.createArrayBacked(InventoryDrawCallback.class,
            (listeners) -> (screen, matrices, textRenderer) -> {
                for (InventoryDrawCallback listener : listeners) {
                    listener.render(screen, matrices, textRenderer);
                }
            });

    void render(InventoryScreen screen, PoseStack matrices, Font textRenderer);
}
