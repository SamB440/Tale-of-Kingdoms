package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder.confirm.ConfirmBuildKingdomGui;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CityBuilderBeginGui extends LightweightGuiDescription {

    public CityBuilderBeginGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        WSprite icon = new WSprite(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"));
        root.add(icon, 0, 0, 400, 256);

        //todo: translatable
        root.add(new WLabel(Text.literal("Build Menu Tier 1 - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"), 11111111).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), 200, 10, 16, 2);

        WButton buildButton = new WButton(Text.translatable("menu.taleofkingdoms.citybuilder.build"));
        buildButton.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
            MinecraftClient.getInstance().setScreen(new BaseCityBuilderScreen(new ConfirmBuildKingdomGui(player, entity, instance)));
        });
        buildButton.setAlignment(HorizontalAlignment.CENTER);
        root.add(buildButton, root.getWidth() / 2 - 60, root.getHeight() / 2 + 35, 120, 30);

        WButton exitButton = new WButton(Text.literal("Exit"));
        exitButton.setOnClick(() -> MinecraftClient.getInstance().currentScreen.close());
        root.add(exitButton, root.getWidth() / 2 - 25, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}