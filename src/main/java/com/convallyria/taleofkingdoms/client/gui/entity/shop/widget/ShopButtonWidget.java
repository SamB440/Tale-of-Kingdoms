package com.convallyria.taleofkingdoms.client.gui.entity.shop.widget;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;


public class ShopButtonWidget extends ButtonWidget {

    private final ShopScreenInterface shopScreen;
    private final ShopItem shopItem;
    protected final int width;
    private final boolean enabled = true;
    private final TextRenderer textRenderer;

    public ShopButtonWidget(@NotNull ShopItem shopItem, @NotNull ShopScreenInterface shopScreen, int x, int y, TextRenderer textRenderer) {
        super(x, y, 110, 20, Text.literal("Buy Button"), button -> shopScreen.setSelectedItem(shopItem), textSupplier -> Text.empty());
        this.textRenderer = textRenderer;
        this.shopScreen = shopScreen;
        this.shopItem = shopItem;
        this.width = 110;
        this.height = 20;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Identifier identifier = new Identifier(TaleOfKingdoms.MODID,"textures/gui/gui.png");
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, identifier);
        boolean flag = isMouseOver(mouseX, mouseY);
        int k;
        if (shopScreen.getSelectedItem().equals(shopItem)) {
            k = 2;
        } else {
            k = 1;
        }

        drawTexture(matrices, this.x(), this.y(), 0, 46 + k * 20, width / 2, height);
        drawTexture(matrices, this.x() + width / 2, this.y(), 200 - width / 2, 46 + k * 20, width / 2, height);
        super.mouseDragged(mouseX, mouseY, 0, delta, delta); // Don't know what deltaX and deltaY are.
        if (!enabled) {
            drawCenteredTextWithShadow(matrices, textRenderer, shopItem.getName(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0xffffcc00);
        } else if (!flag) {
            drawCenteredTextWithShadow(matrices, textRenderer, shopItem.getName(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0xffffff);
        } else {
            drawCenteredTextWithShadow(matrices, textRenderer, shopItem.getName(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0x00cc00);
        }
    }
}