package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CityBuilderPriceListGui extends BaseCityBuilderScreen {

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public CityBuilderPriceListGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.CITYBUILDER_GUI_OPEN.send(player);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        // Starts at - 65 in reality, but we +10 for the first
        int rootHeightPercent = 5;

        // 16447734 = "pastel white"
        for (BuildCosts build : BuildCosts.values()) {
            final MutableText text = build.getDisplayName().copy().append(": " + build.getWood() + " wood " + build.getStone() + " cobblestone");
            rootComponent.child(
                Components.label(text).color(Color.ofRgb(16447734))
                .positioning(Positioning.relative(50, rootHeightPercent += 5))
            );
        }

        rootComponent.child(
            Components.button(
                Text.literal("Back."),
                (ButtonComponent button) -> {
                    this.close();
                    MinecraftClient.getInstance().setScreen(new CityBuilderTierOneGui(player, entity, instance));
                }
            )
            .positioning(Positioning.relative(50, 85))
        );
    }
}