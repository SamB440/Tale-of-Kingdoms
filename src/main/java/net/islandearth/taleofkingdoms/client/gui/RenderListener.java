package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.InventoryDrawCallback;
import net.islandearth.taleofkingdoms.common.listener.Listener;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Optional;

public class RenderListener extends Listener {

    public RenderListener() {
        InventoryDrawCallback.EVENT.register((gui, matrices, textRenderer) -> {
            Optional<ConquestInstance> instance = TaleOfKingdoms
                    .getAPI()
                    .get()
                    .getConquestInstanceStorage()
                    .mostRecentInstance();
            if (!instance.isPresent()) return;

            drawWithoutShadow(matrices, textRenderer, "Gold Coins: " + instance.get().getCoins(), gui.width / 2 - 50, gui.height / 2 - 100, 16763904);
        });
    }

    private void drawWithoutShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        textRenderer.draw(matrices, text, (float) (centerX - textRenderer.getWidth(text) / 2), (float) y, color);
    }
}
