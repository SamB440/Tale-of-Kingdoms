package com.convallyria.taleofkingdoms.client.gui.entity.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.PageTurnWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopScreenInterface;
import com.convallyria.taleofkingdoms.client.gui.shop.Shop;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.client.utils.ShopBuyUtil;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.common.collect.ImmutableList;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultShopScreen extends BaseUIModelScreen<FlowLayout> implements ShopScreenInterface {

    private final PlayerEntity player;
    private final ShopEntity entity;
    private final ConquestInstance instance;

    private final ImmutableList<ShopItem> shopItems;
    private ShopItem selectedItem;
    private Shop shop;

    private LabelComponent coinsLabel, selectedItemLabel;

    public DefaultShopScreen(PlayerEntity player, ShopEntity entity, ConquestInstance instance) {
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "shop_ui_model")));
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
    protected void build(FlowLayout rootComponent) {
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");
        inner.childById(ButtonComponent.class, "buy-button").onPress(button -> {
            int count = 1;
            if (Screen.hasShiftDown()) count = 16;
            ShopBuyUtil.buyItem(instance, player, selectedItem, count, entity);
        });

        this.selectedItem = shopItems.get(0);

        inner.child(this.coinsLabel = (LabelComponent)
            Components.label(Text.literal("Shop Menu - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"))
                .color(Color.WHITE)
                .positioning(Positioning.relative(50, 5))
        );

        inner.child(this.selectedItemLabel = (LabelComponent)
            Components.label(Text.literal("Selected Item Cost: " + this.selectedItem.getName() + " - " + this.selectedItem.getCost() + " Gold Coins"))
                .color(Color.WHITE)
                .positioning(Positioning.relative(50, 10))
        );

        //todo: dynamically generate page turn widgets per-page? how?
        // Unsure how we can get custom widgets to work with owo dynamic ui
        inner.child(new PageTurnWidget(false, button -> {
            shop.previousPage();
        }, true).positioning(Positioning.relative(3, 18)));

        inner.child(new PageTurnWidget(true, button -> {
            shop.nextPage();
        }, true).positioning(Positioning.relative(70, 18)));

        final int maxPerSide = 18;
        int page = 0;
        int currentIteration = 0;
        int currentY = 24;
        int currentX = 10;
        Map<Integer, ShopPage> pages = new HashMap<>();
        pages.put(page, new ShopPage(page));

        for (ShopItem shopItem : shopItems) {
            // Have we done the maximum items for a single row?
            // If so, reset to default y pos, move along x pos.
            if (currentIteration == 9) {
                currentY = 24;
                currentX = currentX + 40;
            }

            // Have we done the maximum items for a single page?
            // If so, increase page num, set x/y to default.
            if (currentIteration >= maxPerSide) {
                page++;
                currentIteration = 0;
                currentY = 24;
                currentX = 10;
                pages.put(page, new ShopPage(page));
            }

            final ShopButtonWidget widget = new ShopButtonWidget(shopItem, this, currentX, currentY, this.textRenderer);
            inner.child(widget.positioning(Positioning.relative(currentX, currentY)));
            pages.get(page).addItem(widget);

            currentY = currentY + 8;
            currentIteration++;
        }

        this.shop = new Shop(pages);
        pages.get(0).show();

        inner.childById(ButtonComponent.class, "sell-button").onPress(button -> openSellGui(entity, player));

        //todo page turn?

        inner.childById(ButtonComponent.class, "exit-button").onPress(button -> this.close());
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        coinsLabel.text(Text.literal("Shop Menu - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"));
        if (this.selectedItem != null) {
            StringBuilder text = new StringBuilder("Selected Item Cost: " + this.selectedItem.getName() + " - " + this.selectedItem.getCost() + " Gold Coins");
            if (this.selectedItem.getModifier() != 1) {
                text.append(" x ").append(String.format("%.2f", this.selectedItem.getModifier())).append(" modifier");
            }
            this.selectedItemLabel.text(Text.literal(text.toString()));
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
