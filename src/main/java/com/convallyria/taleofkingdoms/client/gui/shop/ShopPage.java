package com.convallyria.taleofkingdoms.client.gui.shop;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopButtonWidget;

import java.util.ArrayList;
import java.util.List;

public final class ShopPage {

    private final int page;
    private final List<ShopButtonWidget> buttonWidgets;

    public ShopPage(int page) {
        this.page = page;
        this.buttonWidgets = new ArrayList<>();
    }

    public List<ShopButtonWidget> getItems() {
        return buttonWidgets;
    }

    public int getPage() {
        return page;
    }

    public void addItem(ShopButtonWidget shopButtonWidget) {
        if (buttonWidgets.size() >= 18) throw new IllegalStateException("Size of list cannot be greater than 18");
        buttonWidgets.add(shopButtonWidget);
        shopButtonWidget.visible = false;
    }

    public void removeItem(ShopButtonWidget shopButtonWidget) {
        buttonWidgets.remove(shopButtonWidget);
    }

    public void hide() {
        buttonWidgets.forEach(shopButtonWidget -> shopButtonWidget.visible = false);
    }

    public void show() {
        buttonWidgets.forEach(shopButtonWidget -> shopButtonWidget.visible = true);
    }
}
