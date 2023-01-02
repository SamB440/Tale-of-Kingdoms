package com.convallyria.taleofkingdoms.client.gui.entity.cotton.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.util.Identifier;

public class DefaultShopDescriptor extends LightweightGuiDescription {

    public DefaultShopDescriptor() {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        WSprite icon = new WSprite(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"));
        root.add(icon, 0, -70, 400, 256);
    }

    @Override
    public void addPainters() {}
}
