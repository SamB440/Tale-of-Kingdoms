package com.convallyria.taleofkingdoms.client.gui.image;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Image implements IImage {

    private final Identifier imageIdentifier;
    private final int x;
    private final int y;
    private final int[] dimensions;

    public Image(Identifier imageIdentifier, int x, int y, int[] dimensions) {
        this.imageIdentifier = imageIdentifier;
        this.x = x;
        this.y = y;
        this.dimensions = dimensions;
    }

    @Override
    public Identifier getResourceLocation() {
        return imageIdentifier;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        client.getTextureManager().bindTexture(imageIdentifier);
        gui.drawTexture(matrices, x, y, 0, 0, getWidth(), getHeight());
    }
}
