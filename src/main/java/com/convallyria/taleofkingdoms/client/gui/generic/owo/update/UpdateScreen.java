package com.convallyria.taleofkingdoms.client.gui.generic.owo.update;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UpdateScreen extends BaseUIModelScreen<FlowLayout> {

    public UpdateScreen() {
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "update_ui_model")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
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

        rootComponent.childById(FlowLayout.class, "inner").child(
                Containers.verticalScroll(Sizing.content(), Sizing.fill(50),
                        Components.label(Text.literal(String.join("\n", updates))).color(Color.ofDye(DyeColor.GRAY)))
                .positioning(Positioning.relative(50, 50)));

//        rootComponent.childById(ScrollContainer.class, "scroll").child(Components.label(Text.literal(String.join("\n", updates))).color(Color.ofDye(DyeColor.GRAY)));

        rootComponent.childById(ButtonComponent.class, "exit-button").onPress(b -> {
            this.close();
        });
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
