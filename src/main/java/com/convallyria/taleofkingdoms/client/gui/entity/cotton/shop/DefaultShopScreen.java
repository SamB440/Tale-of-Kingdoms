package com.convallyria.taleofkingdoms.client.gui.entity.cotton.shop;

import com.convallyria.taleofkingdoms.client.gui.entity.widget.PageTurnWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopScreenInterface;
import com.convallyria.taleofkingdoms.client.gui.shop.Shop;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.client.utils.ShopBuyUtil;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.common.collect.ImmutableList;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultShopScreen extends CottonClientScreen implements ShopScreenInterface {

    private final PlayerEntity player;
    private final ShopEntity entity;
    private final ConquestInstance instance;

    private final ImmutableList<ShopItem> shopItems;
    private ShopItem selectedItem;
    private Shop shop;

    public DefaultShopScreen(PlayerEntity player, ShopEntity entity, ConquestInstance instance) {
        super(new DefaultShopDescriptor());
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.shopItems = entity.getShopItems();
    }

    @Override
    public ShopItem getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(ShopItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Buy"), widget -> {
            int count = 1;
            if (Screen.hasShiftDown()) count = 16;
            ShopBuyUtil.buyItem(instance, player, selectedItem, count, entity);
        }).dimensions(this.width / 2 + 132, this.height / 2 - 55, 55, 20)
                .tooltip(Tooltip.of(Text.literal("Use Left Shift to buy 16x."))).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Sell"), widget -> {
            openSellGui(entity, player);
        }).dimensions(this.width / 2 + 132, this.height / 2 - 30, 55, 20).build());

        this.addDrawableChild(new PageTurnWidget(this.width / 2 - 135, this.height / 2 - 100, false, button -> shop.previousPage(), true));
        this.addDrawableChild(new PageTurnWidget(this.width / 2 + 130, this.height / 2 - 100, true, button -> shop.nextPage(), true));

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Exit"), widget -> {
            this.close();
        }).dimensions(this.width / 2 - 160, this.height / 2 + 20, 45, 20).build());

        this.selectedItem = shopItems.get(0);

        final int maxPerSide = 18;
        int page = 0;
        int currentIteration = 0;
        int currentY = this.height / 4;
        int currentX = this.width / 2 - 100;
        Map<Integer, ShopPage> pages = new HashMap<>();
        pages.put(page, new ShopPage(page));

        for (ShopItem shopItem : shopItems) {
            if (currentIteration == 9) {
                currentY = this.height / 4;
                currentX = currentX + 115;
            }

            if (currentIteration >= maxPerSide) {
                page++;
                currentIteration = 0;
                currentY = this.height / 4;
                currentX = this.width / 2 - 100;
                pages.put(page, new ShopPage(page));
            }

            ShopButtonWidget shopButtonWidget = this.addDrawableChild(new ShopButtonWidget(shopItem, this, currentX, currentY, this.textRenderer));
            pages.get(page).addItem(shopButtonWidget);

            currentY = currentY + 20;
            currentIteration++;
        }

        this.shop = new Shop(pages);
        pages.get(0).show();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        drawCenteredText(stack, this.textRenderer, "Shop Menu - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        if (this.selectedItem != null) {
            StringBuilder text = new StringBuilder("Selected Item Cost: " + this.selectedItem.getName() + " - " + this.selectedItem.getCost() + " Gold Coins");
            if (this.selectedItem.getModifier() != 1) {
                text.append(" x ").append(String.format("%.2f", this.selectedItem.getModifier())).append(" modifier");
            }
            drawCenteredText(stack, this.textRenderer, text.toString(), this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        }
    }

    @Override
    public void close() {
        super.close();
        Translations.SHOP_CLOSE.send(player);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
