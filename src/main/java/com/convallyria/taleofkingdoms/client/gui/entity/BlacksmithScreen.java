package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.PageTurnWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopScreenInterface;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.gui.image.ScaleSize;
import com.convallyria.taleofkingdoms.client.gui.shop.Shop;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.shop.ArrowShopItem;
import com.convallyria.taleofkingdoms.common.shop.BowShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShieldShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.StonePickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenSwordShopItem;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlacksmithScreen extends ScreenTOK implements ShopScreenInterface {

    private final PlayerEntity player;
    private final BlacksmithEntity entity;
    private final ClientConquestInstance instance;

    private final ImmutableList<ShopItem> shopItems;
    private ShopItem selectedItem;
    private Shop shop;

    private static final ImmutableList<ScaleSize> SCALE_SIZES = ImmutableList.of(
            new ScaleSize(1, 810, 265),
            new ScaleSize(2, 310, 113),
            new ScaleSize(3, 170, 55),
            new ScaleSize(4, 100, 27));

    private static final ImmutableList<ScaleSize> SCALE_SIZES_TWO = ImmutableList.of(
            new ScaleSize(1, 1000, 265),
            new ScaleSize(2, 540, 113),
            new ScaleSize(3, 360, 55),
            new ScaleSize(4, 275, 27));

    public BlacksmithScreen(PlayerEntity player, BlacksmithEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.blacksmith.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.shopItems = ImmutableList.of(new ArrowShopItem(), new BowShopItem(),
                new DiamondAxeShopItem(), new DiamondBootsShopItem(),
                new DiamondChestplateShopItem(), new DiamondHelmetShopItem(), new DiamondLeggingsShopItem(), new DiamondPickaxeShopItem(),
                new DiamondShovelShopItem(), new DiamondSwordShopItem(),
                new IronAxeShopItem(), new IronBootsShopItem(), new IronChestplateShopItem(), new IronHelmetShopItem(),
                new IronLeggingsShopItem(), new IronPickaxeShopItem(), new IronShovelShopItem(), new IronSwordShopItem(),
                new LeatherBootsShopItem(), new LeatherChestplateShopItem(), new LeatherHelmetShopItem(), new LeatherLeggingsShopItem(),
                new ShieldShopItem(), new StoneAxeShopItem(), new StonePickaxeShopItem(), new StoneShovelShopItem(), new StoneSwordShopItem(),
                new WoodenAxeShopItem(), new WoodenPickaxeShopItem(), new WoodenShovelShopItem(), new WoodenSwordShopItem());
        int guiScale = MinecraftClient.getInstance().options.guiScale;
        TaleOfKingdoms.LOGGER.info("a" + guiScale);
        Optional<ScaleSize> scaleSize = SCALE_SIZES.stream().filter(size -> size.getGuiScale() == guiScale).findFirst();
        if (!scaleSize.isPresent()) return;
        int x = scaleSize.get().getX();
        int y = scaleSize.get().getY();
        Optional<ScaleSize> scaleSizeTwo = SCALE_SIZES_TWO.stream().filter(size -> size.getGuiScale() == guiScale).findFirst();
        if (!scaleSizeTwo.isPresent()) return;
        int xTwo = scaleSizeTwo.get().getX();
        int yTwo = scaleSizeTwo.get().getY();
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu1.png"), x, y, new int[]{230, 230}));
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu2.png"), xTwo, yTwo, new int[]{230, 230}));
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
        this.addButton(new ButtonWidget(this.width / 2 + 132 , this.height / 2 - 55, 55, 20, new LiteralText("Buy"), button -> {
            selectedItem.buy(instance, player);
        }));

        this.addButton(new ButtonWidget(this.width / 2 + 132, this.height / 2 - 30 , 55, 20, new LiteralText("Sell"), button -> this.onClose()));
        this.addButton(new PageTurnWidget(this.width / 2 - 135, this.height / 2 - 100, false, button -> shop.previousPage(), true));
        this.addButton(new PageTurnWidget(this.width / 2 + 130, this.height / 2 - 100, true, button -> shop.nextPage(), true));
        this.addButton(new ButtonWidget(this.width / 2 - 160 , this.height / 2 + 20, 45, 20, new LiteralText("Exit"), button -> this.onClose()));

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
