package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.confirm.ConfirmBuildKingdomGui;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CityBuilderBeginGui extends BaseCityBuilderScreen {

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "textures/gui/menu.png");

    private final Player player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public CityBuilderBeginGui(Player player, CityBuilderEntity entity, ConquestInstance instance) {
        super(DataSource.asset(ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "citybuilder_begin_model")));
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
                Components.label(Component.translatable("menu.taleofkingdoms.citybuilder.total_money", "Tier 1", guildPlayer.getCoins()))
                        .positioning(Positioning.relative(50, 5))
        );

        inner.child(
                Components.button(Component.translatable("menu.taleofkingdoms.citybuilder.build"), c -> {
                    Minecraft.getInstance().screen.onClose();
                    Minecraft.getInstance().setScreen(new ConfirmBuildKingdomGui(player, entity, instance));
                })
                .positioning(Positioning.relative(50, 75))
        );

        inner.child(
                Components.button(
                    Component.translatable("menu.taleofkingdoms.generic.exit"),
                    (ButtonComponent button) -> this.onClose()
                )
                .positioning(Positioning.relative(50, 85))
        );
    }
}