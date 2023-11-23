package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CityBuilderPriceListGui extends BaseCityBuilderScreen {

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;
    private final PlayerKingdom kingdom;

    public CityBuilderPriceListGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance, PlayerKingdom kingdom) {
        super(DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "citybuilder_price_list_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.kingdom = kingdom;
        Translations.CITYBUILDER_GUI_OPEN.send(player);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        // Starts at - 65 in reality, but we +10 for the first
        int rootHeightPercent = 5;

        // 16447734 = "pastel white"
        for (BuildCosts build : BuildCosts.values()) {
            if (!kingdom.getTier().isHigherThanOrEqual(build.getTier())) continue;
            final MutableText text = build.getDisplayName().copy().append(": " + build.getWood() + " wood " + build.getStone() + " cobblestone");
            rootComponent.child(
                Components.label(text).color(Color.ofRgb(16447734))
                .positioning(Positioning.relative(50, rootHeightPercent += 5))
            );
        }

        rootComponent.child(
            Components.button(
                Text.translatable("menu.taleofkingdoms.generic.back"),
                (ButtonComponent button) -> {
                    this.close();
                    MinecraftClient.getInstance().setScreen(new CityBuilderTierOneGui(player, entity, instance));
                }
            )
            .positioning(Positioning.relative(50, 85))
        );
    }
}