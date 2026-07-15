package com.convallyria.taleofkingdoms.client.gui.entity.shop.widget;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class ShopButtonWidget extends Button {

    private final ShopScreenInterface shopScreen;
    private final ShopItem shopItem;
    protected final int width;
    private final boolean enabled = true;
    private final Font textRenderer;

    public ShopButtonWidget(@NotNull ShopItem shopItem, @NotNull ShopScreenInterface shopScreen, int x, int y, Font textRenderer) {
        super(x, y, 110, 20, Component.literal("Buy Button"), button -> shopScreen.setSelectedItem(shopItem), textSupplier -> Component.empty());
        this.textRenderer = textRenderer;
        this.shopScreen = shopScreen;
        this.shopItem = shopItem;
        this.width = 110;
        this.height = 20;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID,"textures/gui/gui.png");
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = isMouseOver(mouseX, mouseY);
        int k;
        if (shopScreen.getSelectedItem().equals(shopItem)) {
            k = 2;
        } else {
            k = 1;
        }

        context.blit(identifier, this.x(), this.y(), 0, 46 + k * 20, width / 2, height);
        context.blit(identifier, this.x() + width / 2, this.y(), 200 - width / 2, 46 + k * 20, width / 2, height);
        super.mouseDragged(mouseX, mouseY, 0, delta, delta); // Don't know what deltaX and deltaY are.
        if (!enabled) {
            context.drawCenteredString(textRenderer, shopItem.getItem().getDescription(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0xffffcc00);
        } else if (!flag) {
            context.drawCenteredString(textRenderer, shopItem.getItem().getDescription(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0xffffff);
        } else {
            context.drawCenteredString(textRenderer, shopItem.getItem().getDescription(), (this.x() + width / 2) - 20, this.y() + (height - 8) / 2, 0x00cc00);
        }
    }
}