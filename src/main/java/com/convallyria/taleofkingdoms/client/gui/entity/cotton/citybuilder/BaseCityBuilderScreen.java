package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class BaseCityBuilderScreen extends CottonClientScreen {

    public BaseCityBuilderScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}