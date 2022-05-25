package com.convallyria.taleofkingdoms.client.gui.generic.cotton.update;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UpdateGui extends LightweightGuiDescription {

    public UpdateGui() {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        WSprite icon = new WSprite(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"));
        root.add(icon, 0, 0, 400, 256);

        WBox scrollBox = new WBox(Axis.VERTICAL);
        List<String> updates = new ArrayList<>();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("CHANGELOG.md");
        try (BufferedReader input = new BufferedReader(new InputStreamReader(in))) {
            String update;
            while ((update = input.readLine()) != null) {
                updates.add(update);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String update : updates) {
            scrollBox.add(new WLabel(new LiteralText(update)));
        }

        root.add(new WLabel(new LiteralText("Tale of Kingdoms updates")).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), 200, 10, 8, 2);
        root.add(new WScrollPanel(scrollBox), 50, 50, 300, 100);
        WButton exitButton = new WButton(new LiteralText("Exit"));
        exitButton.setOnClick(() -> MinecraftClient.getInstance().currentScreen.close());
        root.add(exitButton, 178, root.getHeight() / 2 + 40, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}
