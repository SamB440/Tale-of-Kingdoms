package com.convallyria.taleofkingdoms.client.gui.generic.owo.update;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UpdateScreen extends BaseOwoScreen<FlowLayout> {

    private static final Identifier BACKGROUND = new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png");

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::horizontalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(Components.texture(BACKGROUND, 400, 256, 400, 256, 400, 256));

        rootComponent.child(
                Components.label(Text.literal("Tale of Kingdoms updates")).color(Color.ofDye(DyeColor.GRAY))
                .positioning(Positioning.relative(50, 15)));

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

        rootComponent.child(
                Containers.verticalScroll(Sizing.content(), Sizing.fill(50),
                        Components.label(Text.literal(String.join("\n", updates))).color(Color.ofDye(DyeColor.GRAY)))
                .positioning(Positioning.relative(50, 50))
        );

        rootComponent.child(
                Components.button(
                    Text.literal("Exit"),
                    (ButtonComponent button) -> this.close()
                ).positioning(Positioning.relative(50, 85))
        );
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
