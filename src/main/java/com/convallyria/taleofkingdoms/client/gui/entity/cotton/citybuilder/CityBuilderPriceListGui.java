package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CityBuilderPriceListGui extends LightweightGuiDescription {

    public CityBuilderPriceListGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        // Starts at - 65 in reality, but we +10 for the first
        int rootHeight = root.getHeight() / 2 - 75;

        // 0xbcbcbc = "pastel white"
        for (BuildCosts build : BuildCosts.values()) {
            final MutableText text = build.getDisplayName().copy().append(": " + build.getWood() + " wood " + build.getStone() + " cobblestone");
            WLabel label = new WLabel(text, 0xbcbcbc);
            root.add(label, 100, rootHeight += 10, 45, 10);
        }

        WButton exitButton = new WButton(Text.literal("Back."));
        exitButton.setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.close();
            MinecraftClient.getInstance().setScreen(new BaseCityBuilderScreen(new CityBuilderTierOneGui(player, entity, instance)));
        });
        root.add(exitButton, 178, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}