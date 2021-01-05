package com.convallyria.taleofkingdoms.client.gui.shop;

import java.util.Map;

public final class Shop {

    private final Map<Integer, ShopPage> pages;
    private int currentPage = 0;

    public Shop(Map<Integer, ShopPage> pages) {
        this.pages = pages;
    }

    public Map<Integer, ShopPage> getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
