package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.atomic.AtomicInteger;

public class CityBuilderTierOneGui extends LightweightGuiDescription {

    private final CityBuilderEntity entity;
    private final WButton fixWholeKingdomButton;
    private final WLabel oakWoodLabel, cobblestoneLabel;

    public CityBuilderTierOneGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        this.entity = entity;
        final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        WSprite icon = new WSprite(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"));
        root.add(icon, 0, 0, 400, 256);

        //todo: translatable
        root.add(new WLabel(Text.literal("Build Menu Tier 1 - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"), 11111111).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), 200, 10, 16, 2);
        //root.add(new WScrollPanel(scrollBox), 50, 50, 300, 100);

        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getInventory().count(Items.COBBLESTONE));
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getInventory().count(Items.OAK_LOG));

        //root.add(new WLabel(Text.literal("0      160      320")).setHorizontalAlignment(HorizontalAlignment.LEFT), 100, 30, 16, 2);

        this.oakWoodLabel = new WLabel(Text.literal(oakWoodCount + " / 320 oak wood"));
        this.cobblestoneLabel = new WLabel(Text.literal(cobblestoneCount + " / 320 cobblestone"));
        root.add(oakWoodLabel, 100, 27, 32, 20);
        root.add(cobblestoneLabel, 100, 47, 32, 20);

        WButton woodButton = new WButton(Text.literal("Give 64 wood"));
        woodButton.setOnClick(() -> {
            final int playerOakWoodCount = player.getInventory().count(Items.OAK_LOG);
            TaleOfKingdoms.getAPI().executeOnServer(() -> {
                final ItemStack stack = new ItemStack(Items.OAK_LOG, 64);
                if (playerOakWoodCount >= 64 && oakWoodCount.get() <= (320 - 64) && entity.getInventory().canInsert(stack)) {
                    final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                    serverPlayer.getInventory().removeStack(slot);
                    player.getInventory().removeStack(slot);
                    entity.getInventory().addStack(stack);
                    update();
                }
            });
        });
        root.add(woodButton, 222, 20, 100, 10);

        WButton cobbleButton = new WButton(Text.literal("Give 64 cobblestone"));
        cobbleButton.setOnClick(() -> {
            final int playerCobblestoneCount = player.getInventory().count(Items.COBBLESTONE);
            TaleOfKingdoms.getAPI().executeOnServer(() -> {
                final ItemStack stack = new ItemStack(Items.COBBLESTONE, 64);
                if (playerCobblestoneCount >= 64 && cobblestoneCount.get() <= (320 - 64) && entity.getInventory().canInsert(stack)) {
                    final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                    serverPlayer.getInventory().removeStack(slot);
                    player.getInventory().removeStack(slot);
                    entity.getInventory().addStack(stack);
                    update();
                }
            });
        });
        root.add(cobbleButton, 222, 40, 100, 10);

        this.fixWholeKingdomButton = new WButton(Text.literal("Fix whole kingdom"));
        fixWholeKingdomButton.addTooltip(new TooltipBuilder().add(
                Text.literal("Repairing the kingdom costs:"),
                Text.literal(" - 320 oak wood"),
                Text.literal(" - 320 cobblestone")));
        fixWholeKingdomButton.setOnClick(() -> TaleOfKingdoms.getAPI().executeOnServer(() -> {
            if (entity.getInventory().count(Items.COBBLESTONE) != 320 || entity.getInventory().count(Items.OAK_LOG) != 320) return;
            final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
            TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, serverPlayer, kingdom.getOrigin());
            entity.getInventory().clear();
            update();
        }));
        root.add(fixWholeKingdomButton, 222, 70, 100, 10);
        update();

        // Price list
        WButton priceListButton = new WButton(Text.literal("Price List"));
        priceListButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new BaseCityBuilderScreen(new CityBuilderPriceListGui(player, entity, instance))));
        root.add(priceListButton, 222, 100, 100, 10);

        WButton exitButton = new WButton(Text.literal("Exit"));
        exitButton.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
            Translations.CITYBUILDER_GUI_CLOSE.send(player);
        });
        root.add(exitButton, 178, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    private void update() {
        AtomicInteger cobblestoneCount = new AtomicInteger(entity.getInventory().count(Items.COBBLESTONE));
        AtomicInteger oakWoodCount = new AtomicInteger(entity.getInventory().count(Items.OAK_LOG));
        fixWholeKingdomButton.setEnabled(cobblestoneCount.get() == 320 && oakWoodCount.get() == 320);

        oakWoodLabel.setText(Text.literal((oakWoodCount) + " / 320 oak wood"));
        cobblestoneLabel.setText(Text.literal((cobblestoneCount) + " / 320 cobblestone"));
    }

    @Override
    public void addPainters() {}
}