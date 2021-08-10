package com.convallyria.taleofkingdoms.client.gui;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.InventoryDrawCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

public class RenderListener extends Listener {

    public RenderListener() {
        InventoryDrawCallback.EVENT.register((gui, matrices, textRenderer) -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
                drawWithoutShadow(matrices, textRenderer, "Gold Coins: " + instance.getCoins(), gui.width / 2 - 50, gui.height / 2 - 100, 16763904);
            });
        });
    }

    private void drawWithoutShadow(PoseStack matrices, Font textRenderer, String text, int centerX, int y, int color) {
        textRenderer.draw(matrices, text, (float) (centerX - textRenderer.width(text) / 2), (float) y, color);
    }
}
