package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
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


    public CityBuilderTierOneGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
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

        final WLabel oakWoodLabel = new WLabel(Text.literal(oakWoodCount + " / 320 oak wood"));
        final WLabel cobblestoneLabel = new WLabel(Text.literal(cobblestoneCount + " / 320 cobblestone"));
        root.add(oakWoodLabel, 110, 27, 32, 20);
        root.add(cobblestoneLabel, 110, 47, 32, 20);

        WButton woodButton = new WButton(Text.literal("Give 64 wood"));
        woodButton.setOnClick(() -> {
            final int playerOakWoodCount = player.getInventory().count(Items.OAK_LOG);
            TaleOfKingdoms.getAPI().executeOnServer(() -> {
                final ItemStack stack = new ItemStack(Items.OAK_LOG, 64);
                if (playerOakWoodCount >= 64 && oakWoodCount.get() <= (320 - 64) && entity.getInventory().canInsert(stack)) {
                    final ServerPlayerEntity serverPlayer = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    serverPlayer.getInventory().removeOne(stack);
                    entity.getInventory().addStack(stack);
                    cobblestoneLabel.setText(Text.literal((oakWoodCount.addAndGet(64)) + " / 320 oak wood"));
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
                    serverPlayer.getInventory().removeOne(stack);
                    entity.getInventory().addStack(stack);
                    cobblestoneLabel.setText(Text.literal((cobblestoneCount.addAndGet(64)) + " / 320 cobblestone"));
                }
            });
        });
        root.add(cobbleButton, 222, 40, 100, 10);

        WButton exitButton = new WButton(Text.literal("Exit"));
        exitButton.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
            Translations.CITYBUILDER_GUI_CLOSE.send(player);
        });
        root.add(exitButton, 178, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}