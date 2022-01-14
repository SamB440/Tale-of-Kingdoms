package com.convallyria.taleofkingdoms.client.gui.entity.cotton;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class CityBuilderBeginGui extends LightweightGuiDescription {

    public CityBuilderBeginGui() {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        WSprite icon = new WSprite(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"));
        root.add(icon, 0, 0, 400, 256);



        root.add(new WLabel(new LiteralText("City Builder"), 11111111).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), 200, 10, 8, 2);
        //root.add(new WScrollPanel(scrollBox), 50, 50, 300, 100);
        WButton exitButton = new WButton(new LiteralText("Exit"));
        exitButton.setOnClick(() -> MinecraftClient.getInstance().currentScreen.onClose());
        root.add(exitButton, 178, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}
