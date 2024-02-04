package com.convallyria.taleofkingdoms.client.gui.entity.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.PageTurnWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopScreenInterface;
import com.convallyria.taleofkingdoms.client.gui.shop.Shop;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.client.utils.ShopBuyUtil;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.google.common.collect.ImmutableList;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
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
        final GuildPlayer guildPlayer = instance.getPlayer(player);

        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");
        inner.childById(ButtonComponent.class, "buy-button").onPress(button -> {
            int count = 1;
            if (hasShiftDown()) count = 16;
            ShopBuyUtil.buyItem(instance, player, selectedItem, count, entity);
        });

        this.selectedItem = shopItems.get(0);

        inner.child(this.coinsLabel = (LabelComponent)
            Components.label(Text.translatable("menu.taleofkingdoms.shop.total_money", guildPlayer.getCoins()))
                .color(Color.WHITE)
                .positioning(Positioning.relative(50, 5))
        );

        inner.child(this.selectedItemLabel = (LabelComponent)
            Components.label(Text.translatable("menu.taleofkingdoms.shop.select_item_cost", this.selectedItem.getName(), this.selectedItem.getCost()))
                .color(Color.WHITE)
                .positioning(Positioning.relative(50, 10))
        );

        this.buildShopPages(inner);

        inner.childById(ButtonComponent.class, "sell-button").onPress(button -> openSellGui(entity, player));

        //todo page turn?

        inner.childById(ButtonComponent.class, "exit-button").onPress(button -> this.close());
    }

    protected void buildShopPages(FlowLayout inner) {
        //todo: dynamically generate page turn widgets per-page? how?
        // Unsure how we can get custom widgets to work with owo dynamic ui
        inner.child(new PageTurnWidget(false, button -> {
            shop.previousPage();
            this.changePage(shop.getCurrentPage());
        }, true)
                .positioning(Positioning.relative(3, 18)))
                .id("page-left");

        inner.child(new PageTurnWidget(true, button -> {
            shop.nextPage();
            this.changePage(shop.getCurrentPage());
        }, true)
                .positioning(Positioning.relative(70, 18)))
                .id("page-right");

        final int maxPerSide = getMaxPerSide();
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
    }

    public Shop getShop() {
        return shop;
    }

    protected int getMaxPerSide() {
        return 18;
    }

    protected void changePage(int newPage) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        coinsLabel.text(Text.translatable("menu.taleofkingdoms.shop.total_money", instance.getPlayer(player).getCoins()));
        if (this.selectedItem != null) {
            MutableText text = Text.translatable("menu.taleofkingdoms.shop.select_item_cost", this.selectedItem.getName(), this.selectedItem.getCost());
            if (this.selectedItem.getModifier() != 1) {
                text.append(Text.literal(" "));
                text.append(Text.translatable("menu.taleofkingdoms.shop.select_item_cost_modifier", String.format("%.2f", this.selectedItem.getModifier())));
            }
            this.selectedItemLabel.text(text);
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
