package com.convallyria.taleofkingdoms.client.gui.generic.cotton.update;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class UpdateScreen extends CottonClientScreen {

    public UpdateScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}
