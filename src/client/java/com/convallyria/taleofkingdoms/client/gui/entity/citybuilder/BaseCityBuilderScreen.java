package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;

public abstract class BaseCityBuilderScreen extends BaseUIModelScreen<FlowLayout> {

    protected BaseCityBuilderScreen(DataSource source) {
        super(FlowLayout.class, source);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}