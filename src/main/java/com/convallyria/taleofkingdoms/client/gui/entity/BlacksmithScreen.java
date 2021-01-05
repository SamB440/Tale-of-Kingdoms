package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.entity.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.image.IImage;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.shop.*;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.system.CallbackI;

import java.util.Arrays;
import java.util.List;

public class BlacksmithScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final List<IImage> images;
    private final BlacksmithEntity entity;
    private final ClientConquestInstance instance;

    private final ImmutableList<ShopItem> shopItems;
    private ShopItem selectedItem;

    public BlacksmithScreen(PlayerEntity player, BlacksmithEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.blacksmith.name");
        this.player = player;
        this.images = Arrays.asList(new Image(this, new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu1.png"), this.width / 2 + 40, this.height / 2 + 35, new int[]{230, 230}),
                new Image(this, new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu2.png"), this.width / 2 + 75, this.height / 2 + 35, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
        this.shopItems = ImmutableList.of(new IronShovelShopItem(), new IronSwordShopItem());
    }

    public ShopItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ShopItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public ImmutableList<ShopItem> getShopItems() {
        return
                shopItems;
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 40 , this.height / 2 + 35, 75, 20, new LiteralText("Buy Item"), (button) -> {
            if (instance.getCoins() >= selectedItem.getCost()) {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.executeOnMain(() -> {
                        MinecraftServer server = MinecraftClient.getInstance().getServer();
                        if (server != null) {
                            ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                            if (serverPlayerEntity != null) {
                                serverPlayerEntity.inventory.insertStack(new ItemStack(selectedItem.getItem(), 1));
                                instance.setCoins(instance.getCoins() - selectedItem.getCost());
                            }
                        }
                    });
                });
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 120, this.height / 2 + 15, 75, 20, new LiteralText("Sell Item"), (button) -> this.onClose()));
        this.addButton(new ButtonWidget(this.width / 2 - 200, this.height / 2 - 100, 75, 20, new LiteralText("Back"), (button) -> this.onClose()));
        this.addButton(new ButtonWidget(this.width / 2 + 120, this.height / 2 - 100, 75, 20, new LiteralText("Next"), (button) -> this.onClose()));

        this.addButton(new ButtonWidget(this.width / 2 - 200 , this.height / 2 + 15 , 75, 20, new LiteralText("Exit"), (button) -> {
            this.onClose();
        }));

        this.selectedItem = shopItems.get(0);

        int currentY = this.height / 4;
        int currentX = this.width / 2 - 100;
        for (ShopItem shopItem : shopItems) {
            this.addButton(new ShopButtonWidget(shopItem, this, currentX, currentY, this.textRenderer));
            currentY = currentY + 20;
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        images.forEach(image -> image.render(stack, this));
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
