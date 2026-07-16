package com.convallyria.taleofkingdoms.client.gui.generic.bar;

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