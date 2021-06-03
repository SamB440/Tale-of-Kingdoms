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

    public int getMaxPages() {
        return pages.size();
    }

    public void nextPage() {
        final int currentPage = getCurrentPage();
        if (getPages().size() <= currentPage + 1) {
            return;
        }

        getPages().get(currentPage).hide();
        setCurrentPage(currentPage + 1);
        getPages().get(getCurrentPage()).show();
    }

    public void previousPage() {
        final int currentPage = getCurrentPage();
        if (currentPage == 0) return;
        pages.get(currentPage).hide();
        this.currentPage = currentPage - 1;
        setCurrentPage(currentPage - 1);
        getPages().get(getCurrentPage()).show();
    }
}
