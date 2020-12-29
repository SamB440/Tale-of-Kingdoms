package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL40;

public class ScreenBar extends ScreenTOK {

    private final int xPosition;
    private final int borderColor = -10592674;
    private final int yPosition;
    private int width;
    private int height;
    private float barPosition;
    private final boolean border;
    private BarColour colour;

    public ScreenBar(int x, int y, int width, int height, float progress) {
        super("taleofkingdoms.menu.bar.name");
        if (progress >= 0.0F && progress <= 1.0F) {
            this.barPosition = progress;
        } else {
            this.barPosition = 0.0F;
        }
        this.colour = BarColour.RED;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.border = true;
    }

    public ScreenBar(int x, int y, int width, int height, float progress, BarColour barColour) {
        this(x, y, width, height, progress);
        this.colour = barColour;
    }

    public void setBar(float progress) {
        if (progress >= 0.0F && progress <= 1.0F) {
            this.barPosition = progress;
        }
    }

    public void drawBar(MatrixStack stack) {
        GL40.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.border) {
            DrawableHelper.fill(stack, this.xPosition,
                    this.yPosition,
                    this.xPosition + this.width,
                    this.yPosition + this.height,
                    this.borderColor);
        }

        DrawableHelper.fill(stack,this.xPosition + 1,
                this.yPosition + 1,
                this.xPosition + 1 + this.width - 2,
                this.yPosition + 1 + this.height - 2,
                -16777216);
        DrawableHelper.fill(stack,this.xPosition + 1,
                this.yPosition + 1,
                this.xPosition + 1 + (int)(this.barPosition * (this.width - 2)),
                this.yPosition + 1 + this.height - 2, this.colour.getColour());
    }

    @Override
    public boolean mouseClicked(double i, double j, int a) { return (i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height); }

    public enum BarColour {
        RED(-2553077),
        GREEN(-16298223),
        BLUE(-15000608);

        private final int colour;

        BarColour(int colour) {
            this.colour = colour;
        }

        public int getColour() {
            return colour;
        }
    }
}