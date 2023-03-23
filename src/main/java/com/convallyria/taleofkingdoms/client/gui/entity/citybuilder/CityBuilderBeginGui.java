package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.confirm.ConfirmBuildKingdomGui;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Positioning;
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
        super(DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "citybuilder_begin_model")));
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        this.player = player;
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");

        final GuildPlayer guildPlayer = instance.getPlayer(player);
        inner.child(
                Components.label(Text.literal("Build Menu Tier 1 - Total Money: " + guildPlayer.getCoins() + " Gold Coins"))
                        .positioning(Positioning.relative(50, 5))
        );

        inner.child(
                Components.button(Text.translatable("menu.taleofkingdoms.citybuilder.build"), c -> {
                    MinecraftClient.getInstance().currentScreen.close();
                    MinecraftClient.getInstance().setScreen(new ConfirmBuildKingdomGui(player, entity, instance));
                })
                .positioning(Positioning.relative(50, 75))
        );

        inner.child(
                Components.button(
                    Text.literal("Exit"),
                    (ButtonComponent button) -> this.close()
                )
                .positioning(Positioning.relative(50, 85))
        );
    }
}