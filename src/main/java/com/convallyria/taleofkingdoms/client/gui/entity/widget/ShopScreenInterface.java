package com.convallyria.taleofkingdoms.client.gui.entity.widget;

import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import net.minecraft.client.util.math.MatrixStack;

public interface ShopScreenInterface {

    ShopItem getSelectedItem();

    void setSelectedItem(ShopItem selectedItem);

    void drawTexture(MatrixStack matrices, int xPosition, int yPosition, int x, int y, int width, int height);
}