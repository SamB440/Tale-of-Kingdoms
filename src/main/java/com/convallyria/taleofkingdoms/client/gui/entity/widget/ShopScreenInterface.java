package com.convallyria.taleofkingdoms.client.gui.entity.widget;

import com.convallyria.taleofkingdoms.common.shop.ShopItem;

public interface ShopScreenInterface {

    ShopItem getSelectedItem();

    void setSelectedItem(ShopItem selectedItem);
}