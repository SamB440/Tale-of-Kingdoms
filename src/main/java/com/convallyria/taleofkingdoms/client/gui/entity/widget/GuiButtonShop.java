package com.convallyria.taleofkingdoms.client.gui.entity.widget;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.BlacksmithScreen;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL40;


public class GuiButtonShop extends ButtonWidget {

    private BlacksmithScreen blacksmith;
    private ShopItem shopItem;
    protected final int width;
    private final int xPosition;
    private final int yPosition;
    private final boolean enabled;
    private final boolean enabled2;
    private final TextRenderer textRenderer;

    public GuiButtonShop(@NotNull ShopItem shopItem, @NotNull BlacksmithScreen blacksmith, int x, int y, int width, TextRenderer textRenderer) {
        super(x, y, width, 200, new LiteralText("Buy Button"), button -> {
            blacksmith.setSelectedItem(shopItem);
        });
        this.textRenderer = textRenderer;
        this.blacksmith = blacksmith;
        this.shopItem = shopItem;
        width = 200;
        height = 20;
        enabled = true;
        enabled2 = true;
        xPosition = x;
        yPosition = y;
        this.width = width;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        System.out.println("cliccckededd");
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean bl = this.clicked(mouseX, mouseY);
                if (bl) {
                    this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                    this.onClick(mouseX, mouseY);
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
                return false;
            } else {
                this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.onPress();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!enabled2) return;
        Identifier identifier = new Identifier(TaleOfKingdoms.MODID,"textures/gui/gui.png");
        MinecraftClient.getInstance().getTextureManager().bindTexture(identifier);
        GL40.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        int k;
        if (blacksmith.getSelectedItem().equals(shopItem)) {
            k = 2;
        } else {
            k = 1;
        }

        blacksmith.drawTexture(matrices, xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
        // drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
        blacksmith.drawTexture(matrices, xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);
        // drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);
        //super.mouseDragged(mouseX, mouseY); // TODO
        if (!enabled) {
            drawCenteredString(matrices, textRenderer, shopItem.getName(), (xPosition + width / 2) - 20, yPosition + (height - 8) / 2, 0xffffcc00);
        } else if (!flag) {
            drawCenteredString(matrices, textRenderer, shopItem.getName(), (xPosition + width / 2) - 20, yPosition + (height - 8) / 2, 0xffffff);
        } else {
            drawCenteredString(matrices, textRenderer, shopItem.getName(), (xPosition + width / 2) - 20, yPosition + (height - 8) / 2, 0x00cc00);
        }
    }
}