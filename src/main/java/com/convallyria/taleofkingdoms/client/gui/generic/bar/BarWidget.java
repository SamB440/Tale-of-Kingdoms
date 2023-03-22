package com.convallyria.taleofkingdoms.client.gui.generic.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BarWidget extends ButtonWidget {

    private final int borderColor = -10592674;
    private final int width;
    private final int height;
    private float barProgress;
    private final boolean border;
    private BarColour colour;

    public BarWidget(int width, int height, float progress) {
        this(0, 0, width, height, progress);
    }

    public BarWidget(int x, int y, int width, int height, float progress) {
        super(x, y, width, height, Text.empty(), (w) -> {}, (n) -> null);
        if (progress >= 0.0F && progress <= 1.0F) {
            this.barProgress = progress;
        } else {
            this.barProgress = 0.0F;
        }
        this.colour = BarColour.RED;
        this.width = width;
        this.height = height;
        this.border = true;
    }

    public BarWidget(int x, int y, int width, int height, float progress, BarColour barColour) {
        this(x, y, width, height, progress);
        this.colour = barColour;
    }

    public void setBarProgress(float progress) {
        if (progress >= 0.0F && progress <= 1.0F) {
            this.barProgress = progress;
        }
    }

    public void drawBar(MatrixStack stack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.border) {
            DrawableHelper.fill(stack, this.getX(),
                    this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height,
                    this.borderColor);
        }

        DrawableHelper.fill(stack,this.getX() + 1,
                this.getY() + 1,
                this.getX() + 1 + this.width - 2,
                this.getY() + 1 + this.height - 2,
                -16777216);
        DrawableHelper.fill(stack,this.getX() + 1,
                this.getY() + 1,
                this.getX() + 1 + (int)(this.barProgress * (this.width - 2)),
                this.getY() + 1 + this.height - 2, this.colour.getColour());
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        drawBar(matrices);
    }

    @Override
    public boolean mouseClicked(double i, double j, int a) {
        return (i >= this.getX() && j >= this.getY() && i < this.getX() + this.width && j < this.getY() + this.height);
    }
}