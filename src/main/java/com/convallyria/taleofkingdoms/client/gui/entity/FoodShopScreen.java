package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.PageTurnWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopScreenInterface;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.gui.shop.Shop;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.shop.AppleShopItem;
import com.convallyria.taleofkingdoms.common.shop.BeetrootShopItem;
import com.convallyria.taleofkingdoms.common.shop.BreadShopItem;
import com.convallyria.taleofkingdoms.common.shop.CakeShopItem;
import com.convallyria.taleofkingdoms.common.shop.CarrotShopItem;
import com.convallyria.taleofkingdoms.common.shop.CookieShopItem;
import com.convallyria.taleofkingdoms.common.shop.GoldenAppleShopItem;
import com.convallyria.taleofkingdoms.common.shop.MelonShopItem;
import com.convallyria.taleofkingdoms.common.shop.PotatoShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawBeefShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawChickenShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawCodShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawMuttonShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawPorkchopShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawRabbitShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawSalmonShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class FoodShopScreen extends ScreenTOK implements ShopScreenInterface {

    private final PlayerEntity player;
    private final FoodShopEntity entity;
    private final ClientConquestInstance instance;

    private final ImmutableList<ShopItem> shopItems;
    private ShopItem selectedItem;
    private Shop shop;

    public FoodShopScreen(PlayerEntity player, FoodShopEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.foodshop.name");
        this.player = player;
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu1.png"), this.width / 2 + 310, this.height / 2 + 95, new int[]{230, 230}));
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu2.png"), this.width / 2 + 540, this.height / 2 + 95, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
        this.shopItems = ImmutableList.of(new AppleShopItem(), new BeetrootShopItem(), new BreadShopItem(),
                new CakeShopItem(), new CarrotShopItem(), new CookieShopItem(), new GoldenAppleShopItem(),
                new MelonShopItem(), new PotatoShopItem(), new RawBeefShopItem(), new RawChickenShopItem(),
                new RawCodShopItem(), new RawMuttonShopItem(), new RawPorkchopShopItem(), new RawRabbitShopItem(),
                new RawSalmonShopItem());
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
        this.addButton(new ButtonWidget(this.width / 2 + 132, this.height / 2 - 55, 55, 20, new LiteralText("Buy"), button -> {
            selectedItem.buy(instance, player);
        }));

        this.addButton(new ButtonWidget(this.width / 2 + 132, this.height / 2 - 30, 55, 20, new LiteralText("Sell"), button -> this.onClose()));
        this.addButton(new PageTurnWidget(this.width / 2 - 135, this.height / 2 - 100, false, button -> shop.previousPage(), true));
        this.addButton(new PageTurnWidget(this.width / 2 + 130, this.height / 2 - 100, true, button -> shop.nextPage(), true));
        this.addButton(new ButtonWidget(this.width / 2 - 160, this.height / 2 + 20, 45, 20, new LiteralText("Exit"), button -> this.onClose()));

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

            ShopButtonWidget shopButtonWidget = this.addButton(new ShopButtonWidget(shopItem, this, currentX, currentY, this.textRenderer));
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
        drawCenteredString(stack, this.textRenderer, "Shop Menu - Total Money: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        if (this.selectedItem != null) {
            drawCenteredString(stack, this.textRenderer, "Selected Item Cost: " + this.selectedItem.getName() + " - " + this.selectedItem.getCost() + " Gold Coins", this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        Translations.SHOP_CLOSE.send(player);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
