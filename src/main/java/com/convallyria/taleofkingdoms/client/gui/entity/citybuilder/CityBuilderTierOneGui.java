package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private LabelComponent oakWoodLabel, cobblestoneLabel;

    private final Map<BuildCosts, ButtonComponent> buildButtons = new HashMap<>(BuildCosts.values().length);

    public CityBuilderTierOneGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.CITYBUILDER_GUI_OPEN.send(player);
    }

    private void update() {
        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getInventory().count(Items.COBBLESTONE));
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getInventory().count(Items.OAK_LOG));
        fixWholeKingdomButton.active(cobblestoneCount.get() == 320 && oakWoodCount.get() == 320);

        oakWoodLabel.text(Text.literal((oakWoodCount) + " / 320 oak wood"));
        cobblestoneLabel.text(Text.literal((cobblestoneCount) + " / 320 cobblestone"));

        buildButtons.forEach((build, button) -> button.active(entity.canAffordBuild(build)));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        final FlowLayout inner = rootComponent.child(Containers.horizontalFlow(Sizing.fixed(400), Sizing.fixed(256)));

        // u and v is the position in the texture
        inner.child(Components.texture(BACKGROUND, 400, 256, 400, 256, 400, 256));

        final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());

        inner.child(
            Components.label(Text.literal("Build Menu Tier 1 - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins")).color(Color.ofRgb(11111111))
            .positioning(Positioning.relative(50, 5))
        );

        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getInventory().count(Items.COBBLESTONE));
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getInventory().count(Items.OAK_LOG));

        //root.add(new WLabel(Text.literal("0      160      320")).setHorizontalAlignment(HorizontalAlignment.LEFT), 100, 30, 16, 2);

        inner.child(
            Components.button(Text.literal("Give 64 wood"), c -> {
                final int playerWoodCount = InventoryUtils.count(player.getInventory(), ItemTags.LOGS);
                TaleOfKingdoms.getAPI().executeOnServer(() -> {
                    final ItemStack stack = InventoryUtils.getStack(player.getInventory(), ItemTags.LOGS, 64);
                    if (stack != null && playerWoodCount >= 64 && oakWoodCount.get() <= (320 - 64) && entity.getInventory().canInsert(stack)) {
                        final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                        int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                        serverPlayer.getInventory().removeStack(slot);
                        player.getInventory().removeStack(slot);
                        oakWoodCount.addAndGet(64);
                        entity.getInventory().addStack(new ItemStack(Items.OAK_LOG, 64));
                        update();
                    }
                });
            }).positioning(Positioning.relative(80, 20)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        inner.child(
            Components.button(Text.literal("Give 64 cobblestone"), c -> {
                final int playerCobblestoneCount = player.getInventory().count(Items.COBBLESTONE);
                TaleOfKingdoms.getAPI().executeOnServer(() -> {
                    final ItemStack stack = new ItemStack(Items.COBBLESTONE, 64);
                    if (playerCobblestoneCount >= 64 && cobblestoneCount.get() <= (320 - 64) && entity.getInventory().canInsert(stack)) {
                        final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                        int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                        serverPlayer.getInventory().removeStack(slot);
                        player.getInventory().removeStack(slot);
                        cobblestoneCount.addAndGet(64);
                        entity.getInventory().addStack(stack);
                        update();
                    }
                });
            }).positioning(Positioning.relative(80, 30)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        inner.child(
                this.oakWoodLabel = (LabelComponent) Components.label(Text.literal(oakWoodCount + " / 320 oak wood"))
                        .positioning(Positioning.relative(80, 40))
        );

        inner.child(
                this.cobblestoneLabel = (LabelComponent) Components.label(Text.literal(cobblestoneCount + " / 320 cobblestone"))
                        .positioning(Positioning.relative(80, 45))
        );

        inner.child(
            this.fixWholeKingdomButton = (ButtonComponent) Components.button(Text.literal("Fix whole kingdom"), c -> TaleOfKingdoms.getAPI().executeOnServer(() -> {
                if (entity.getInventory().count(Items.COBBLESTONE) != 320 || entity.getInventory().count(Items.OAK_LOG) != 320)
                    return;
                final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, serverPlayer, kingdom.getOrigin());
                for (BuildCosts buildCost : BuildCosts.values()) {
                    final KingdomPOI kingdomPOI = buildCost.getKingdomPOI();
                    final Schematic schematic = buildCost.getSchematic();
                    if (kingdom.hasBuilt(kingdomPOI)) {
                        TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(schematic, serverPlayer, kingdom.getPOIPos(kingdomPOI));
                    }
                }
                entity.getInventory().clear();
                update();
            })).tooltip(List.of(
                  Text.literal("Repairing the kingdom costs:"),
                  Text.literal(" - 320 oak wood"),
                  Text.literal(" - 320 cobblestone")
            )).positioning(Positioning.relative(80, 70)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        update();

        // Price list
        inner.child(
            Components.button(Text.literal("Price List"), c -> {
                MinecraftClient.getInstance().setScreen(new CityBuilderPriceListGui(player, entity, instance));
            }).positioning(Positioning.relative(80, 80)).sizing(Sizing.fixed(100), Sizing.fixed(20))
        );

        // Actually starts at 70, first has addition of +20
        int currentY = 15;
        int currentX = 15;
        final int maxPerRow = 7;
        int currentRow = 0;
        for (BuildCosts build : BuildCosts.values()) {
            if (currentRow >= maxPerRow) {
                currentRow = 0;
                currentY = 15;
                currentX += 30;
            }

            //todo: small houses / large houses require special changes
            final boolean hasBuilt = kingdom.hasBuilt(build.getKingdomPOI());
            final boolean canAffordBuild = entity.canAffordBuild(build);
            final String text = hasBuilt ? "Fix " : "Build ";
            final String pluralText = hasBuilt ? "Repairing " : "Building ";

            final Component button = Components.button(Text.literal(text).append(build.getDisplayName()), c -> {
                final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                MinecraftClient.getInstance().currentScreen.close();
                kingdom.addBuilt(build.getKingdomPOI());
                TaleOfKingdoms.LOGGER.info("Placing " + build + "...");
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(build.getSchematic(), serverPlayer, kingdom.getPOIPos(build.getKingdomPOI()), build.getSchematicRotation());
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
}