package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder;

import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CityBuilderPriceListGui extends LightweightGuiDescription {

    public CityBuilderPriceListGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());
        Translations.CITYBUILDER_GUI_OPEN.send(player);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        // 0xbcbcbc = "pastel white"
        WLabel smallHouses = new WLabel(Text.literal("Small Houses: 192 wood 128 cobblestone"), 0xbcbcbc);
        WLabel largeHouses = new WLabel(Text.literal("Large Houses: 192 wood 320 cobblestone"), 0xbcbcbc);
        WLabel itemShop = new WLabel(Text.literal("Item Shop: 256 wood 256 cobblestone"), 0xbcbcbc);
        WLabel stockMarket = new WLabel(Text.literal("Stock Market: 192 wood 192 cobblestone"), 0xbcbcbc);
        WLabel builderHouse = new WLabel(Text.literal("Builder House: 128 wood 128 cobblestone"), 0xbcbcbc);
        WLabel blockShop = new WLabel(Text.literal("Block Shop: 256 wood 320 cobblestone"), 0xbcbcbc);
        WLabel foodShop = new WLabel(Text.literal("Food Shop: 192 wood 256 cobblestone"), 0xbcbcbc);
        WLabel barracks = new WLabel(Text.literal("Barracks: 320 wood 320 cobblestone"), 0xbcbcbc);
        WLabel tavern = new WLabel(Text.literal("Tavern: 320 wood 128 cobblestone"), 0xbcbcbc);
        WLabel chapel = new WLabel(Text.literal("Chapel: 320 wood 320 cobblestone"), 0xbcbcbc);
        WLabel library = new WLabel(Text.literal("Library: 256 wood 256 cobblestone"), 0xbcbcbc);
        WLabel mageHall = new WLabel(Text.literal("Mage Hall: 256 wood 320 cobblestone"), 0xbcbcbc);

        int rootHeight = root.getHeight() / 2 - 65;
        root.add(smallHouses, 100, rootHeight, 45, 10);
        root.add(largeHouses, 100, rootHeight + 10, 45, 10);
        root.add(itemShop, 100, rootHeight + 20, 45, 10);
        root.add(stockMarket, 100, rootHeight + 30, 45, 10);
        root.add(builderHouse, 100, rootHeight + 40, 45, 10);
        root.add(blockShop, 100, rootHeight + 50, 45, 10);
        root.add(foodShop, 100, rootHeight + 60, 45, 10);
        root.add(barracks, 100, rootHeight + 70, 45, 10);
        root.add(tavern, 100, rootHeight + 80, 45, 10);
        root.add(chapel, 100, rootHeight + 90, 45, 10);
        root.add(library, 100, rootHeight + 100, 45, 10);
        root.add(mageHall, 100, rootHeight + 110, 45, 10);

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