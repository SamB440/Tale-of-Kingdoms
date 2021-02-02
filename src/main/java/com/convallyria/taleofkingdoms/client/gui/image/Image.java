package com.convallyria.taleofkingdoms.client.gui.image;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Image implements IImage {

    private final ScreenTOK screen;
    private final Identifier image;
    private final int x;
    private final int y;
    private final int[] dimensions;

    public Image(ScreenTOK screen, Identifier image, int x, int y, int[] dimensions) {
        this.screen = screen;
        this.image = image;
        this.x = x;
        this.y = y;
        this.dimensions = dimensions;
    }

    @Override
    public Identifier getResourceLocation() {
        return image;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return dimensions[0];
    }

    public int getHeight() {
        return dimensions[1];
    }

    @Override
    public void render(MatrixStack matrices, Screen gui) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getTextureManager().bindTexture(image);
        gui.drawTexture(matrices, x, y, 0, 0, getWidth(), getHeight());
    }
}
