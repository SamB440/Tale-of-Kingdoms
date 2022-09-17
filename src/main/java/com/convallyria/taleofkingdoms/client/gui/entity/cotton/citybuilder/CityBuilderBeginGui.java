package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder.confirm.ConfirmBuildKingdomGui;
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
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CityBuilderBeginGui extends LightweightGuiDescription {


    public CityBuilderBeginGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
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

        final int cobblestoneAmount = entity.getInventory().count(Items.COBBLESTONE);
        final int oakWoodCount = entity.getInventory().count(Items.OAK_WOOD);

        root.add(new WLabel(Text.literal("0      160      320")).setHorizontalAlignment(HorizontalAlignment.LEFT), 100, 30, 16, 2);

        root.add(new WLabel(Text.literal(cobblestoneAmount + " / 320 cobblestone")), 100, 50, 32, 10);
        root.add(new WLabel(Text.literal(oakWoodCount + " / 320 oak wood")), 100, 30, 32, 10);

        if (kingdom == null) {
            WButton buildButton = new WButton(Text.translatable("menu.taleofkingdoms.citybuilder.build"));
            buildButton.setOnClick(() -> {
                MinecraftClient.getInstance().currentScreen.close();
                MinecraftClient.getInstance().setScreen(new BaseCityBuilderScreen(new ConfirmBuildKingdomGui(player, entity, instance)));
            });
            root.add(buildButton, 153, root.getHeight() / 2 + 35, 120, 30);
        }

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