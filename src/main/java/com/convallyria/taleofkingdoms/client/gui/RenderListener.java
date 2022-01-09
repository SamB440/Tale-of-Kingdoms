package com.convallyria.taleofkingdoms.client.gui;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.InventoryDrawCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderListener extends Listener {

    public RenderListener() {
        InventoryDrawCallback.EVENT.register((gui, matrices, textRenderer) -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                drawWithoutShadow(matrices, textRenderer, "Gold Coins: " + instance.getCoins(), gui.width / 2 - 50, gui.height / 2 - 100, 16763904);
            });
        });
    }

    private void drawWithoutShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        textRenderer.draw(matrices, text, (float) (centerX - textRenderer.getWidth(text) / 2), (float) y, color);
    }
}
