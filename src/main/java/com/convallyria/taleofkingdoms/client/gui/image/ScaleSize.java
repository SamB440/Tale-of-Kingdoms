package com.convallyria.taleofkingdoms.client.gui.image;

public class ScaleSize {

    private final int guiScale;
    private final int x;
    private final int y;

    public ScaleSize(final int guiScale, final int x, final int y) {
        this.guiScale = guiScale;
        this.x = x;
        this.y = y;
    }

    public int getGuiScale() {
        return guiScale;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
