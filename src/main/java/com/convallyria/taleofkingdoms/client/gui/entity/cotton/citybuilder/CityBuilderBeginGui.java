package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder.confirm.ConfirmBuildKingdomGui;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CityBuilderBeginGui extends BaseCityBuilderScreen {

    private static final Identifier BACKGROUND = new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png");

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public CityBuilderBeginGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        this.player = player;
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(Components.texture(BACKGROUND, 400, 256, 400, 256, 400, 256));

        rootComponent.child(
                Components.label(Text.literal("Build Menu Tier 1 - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"))
                        .positioning(Positioning.relative(50, 25))
        );

        rootComponent.child(
                Components.button(Text.translatable("menu.taleofkingdoms.citybuilder.build"), c -> {
                    MinecraftClient.getInstance().currentScreen.close();
                    MinecraftClient.getInstance().setScreen(new ConfirmBuildKingdomGui(player, entity, instance));
                })
                .positioning(Positioning.relative(50, 75))
        );

        rootComponent.child(
                Components.button(
                    Text.literal("Exit"),
                    (ButtonComponent button) -> this.close()
                )
                .positioning(Positioning.relative(50, 85))
        );
    }
}