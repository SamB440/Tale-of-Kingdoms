package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.generic.bar.BarWidget;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.action.CityBuilderAction;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CityBuilderTierOneGui extends BaseCityBuilderScreen {

    private static final Identifier BACKGROUND = new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png");

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;
    private ButtonComponent fixWholeKingdomButton;
    private BarWidget woodBar, stoneBar;
//    private LabelComponent oakWoodLabel, cobblestoneLabel;

    private final Map<BuildCosts, ButtonComponent> buildButtons = new HashMap<>(BuildCosts.values().length);

    public CityBuilderTierOneGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        super(DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "citybuilder_tier_one_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.CITYBUILDER_GUI_OPEN.send(player);
    }

    private void update() {
        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getStone());
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getWood());
        fixWholeKingdomButton.active(cobblestoneCount.get() == 320 && oakWoodCount.get() == 320);

        final float oakWoodPercent = oakWoodCount.get() * (100f / 320f);
        final float cobblestonePercent = cobblestoneCount.get() * (100f / 320f);
        woodBar.setBarProgress(oakWoodPercent / 100);
        woodBar.tooltip(Text.literal(oakWoodCount.get() + " / 320"));
        stoneBar.setBarProgress(cobblestonePercent / 100);
        stoneBar.tooltip(Text.literal(cobblestoneCount.get() + " / 320"));

        buildButtons.forEach((build, button) -> button.active(entity.canAffordBuild(build)));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");

        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final PlayerKingdom kingdom = guildPlayer.getKingdom();

        inner.child(
            Components.label(Text.literal("Build Menu Tier 1 - Total Money: " + guildPlayer.getCoins() + " Gold Coins")).color(Color.ofRgb(11111111))
            .positioning(Positioning.relative(50, 5))
        );

        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getStone());
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getWood());

        //root.add(new WLabel(Text.literal("0      160      320")).setHorizontalAlignment(HorizontalAlignment.LEFT), 100, 30, 16, 2);

        inner.child(
            Components.button(Text.literal("Give 64 wood"), c -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdoms.getAPI().getClientPacketHandler(Packets.CITYBUILDER_ACTION)
                            .handleOutgoingPacket(player, entity.getId(), CityBuilderAction.GIVE_64_WOOD);
                    return;
                }

                entity.give64wood(player);
            }).positioning(Positioning.relative(80, 20)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        inner.child(
            Components.button(Text.literal("Give 64 cobblestone"), c -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdoms.getAPI().getClientPacketHandler(Packets.CITYBUILDER_ACTION)
                            .handleOutgoingPacket(player, entity.getId(), CityBuilderAction.GIVE_64_STONE);
                    return;
                }

                entity.give64stone(player);
            }).positioning(Positioning.relative(80, 30)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        inner.child(
            Components.label(Text.literal("Wood Amount"))
                    .positioning(Positioning.relative(76, 40))
        );

        final float oakWoodPercent = oakWoodCount.get() * (100f / 320f);
        inner.child(this.woodBar = (BarWidget) new BarWidget(0, 0, 100, 12, oakWoodPercent / 100)
                .positioning(Positioning.relative(80, 45))
                .tooltip(Text.literal(oakWoodCount.get() + " / 320")));

        inner.child(
                Components.label(Text.literal("Stone Amount"))
                        .positioning(Positioning.relative(76, 53))
        );

        final float cobblestonePercent = cobblestoneCount.get() * (100f / 320f);
        inner.child(this.stoneBar = (BarWidget) new BarWidget(0, 0, 100, 12, cobblestonePercent / 100)
                .positioning(Positioning.relative(80, 57))
                .tooltip(Text.literal(cobblestoneCount.get() + " / 320")));

        inner.child(
            this.fixWholeKingdomButton = (ButtonComponent) Components.button(Text.literal("Fix whole kingdom"), c -> TaleOfKingdoms.getAPI().executeOnServer(() -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdoms.getAPI().getClientPacketHandler(Packets.CITYBUILDER_ACTION)
                            .handleOutgoingPacket(player, entity.getId(), CityBuilderAction.FIX_KINGDOM);
                    return;
                }

                entity.fixKingdom(player, kingdom);
            })).tooltip(List.of(
                  Text.literal("Repairing the kingdom costs:"),
                  Text.literal(" - 320 oak wood"),
                  Text.literal(" - 320 cobblestone")
            )).positioning(Positioning.relative(80, 70)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        // Price list
        inner.child(
            Components.button(Text.literal("Price List"), c -> {
                MinecraftClient.getInstance().setScreen(new CityBuilderPriceListGui(player, entity, instance));
            }).positioning(Positioning.relative(80, 80)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        // Actually starts at 70, first has addition of +20
        int currentY = 15;
        int currentX = 10;
        final int maxPerRow = 7;
        int currentRow = 0;
        for (BuildCosts build : BuildCosts.values()) {
            if (currentRow >= maxPerRow) {
                currentRow = 0;
                currentY = 15;
                currentX += 35;
            }

            //todo: small houses / large houses require special changes
            final boolean hasBuilt = kingdom.hasBuilt(build.getKingdomPOI());
            final boolean canAffordBuild = entity.canAffordBuild(build);
            final String text = hasBuilt ? "Fix " : "Build ";
            final String pluralText = hasBuilt ? "Repairing " : "Building ";

            final Component button = Components.button(Text.literal(text).append(build.getDisplayName()), c -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdoms.getAPI().getClientPacketHandler(Packets.CITYBUILDER_ACTION)
                            .handleOutgoingPacket(player, entity.getId(), CityBuilderAction.BUILD, build);
                    return;
                }

                entity.build(player, build, kingdom);
            }).active(canAffordBuild)
                    .tooltip(List.of(
                        Text.literal(pluralText).append(build.getDisplayName()).append(Text.literal(" costs:")),
                        Text.literal(" - " + build.getWood() + " oak wood"),
                        Text.literal(" - " + build.getStone() + " cobblestone")
                    ))
                    .positioning(Positioning.relative(currentX, currentY += 8))
                    .sizing(Sizing.fixed(100), Sizing.fixed(20));

            inner.child(button);

            buildButtons.put(build, (ButtonComponent) button);
            currentRow++;
        }

        inner.child(
            Components.button(
                Text.literal("Exit"),
                (ButtonComponent button) -> {
                    this.close();
                    Translations.CITYBUILDER_GUI_CLOSE.send(player);
                }
            ).positioning(Positioning.relative(50, 85))
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.update();
    }
}