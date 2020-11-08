package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.gui.image.IImage;
import net.islandearth.taleofkingdoms.client.gui.image.Image;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.guild.BankerEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class BankerScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final List<IImage> images;
    private final BankerEntity entity;
    private final ConquestInstance instance;

    // Text fields
    private TextFieldWidget text;

    public BankerScreen(PlayerEntity player, BankerEntity entity, ConquestInstance instance) {
        super("menu.taleofkingdoms.banker.name");
        this.player = player;
        this.images = Collections.singletonList(new Image(this, new Identifier(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), 360, 100, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    public void init() {
        super.init();
        this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 77, this.height / 2 - 85, 150, 20, new LiteralText("0"));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 - 20, 150, 20, new LiteralText("Deposit"), (button) -> {
            int coins = Integer.parseInt(this.text.getText());
            if (instance.getCoins() <= coins) {
                instance.setCoins(instance.getCoins() - coins);
                instance.setBankerCoins(instance.getBankerCoins() + coins);
                this.onClose();
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 5, 150, 20, new LiteralText("Withdraw"), (button) -> {
            int coins = Integer.parseInt(this.text.getText());
            if (instance.getBankerCoins() >= coins) {
                instance.setBankerCoins(instance.getBankerCoins() - coins);
                instance.addCoins(coins);
                this.onClose();
            }
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 30, 150, 20, new LiteralText("Exit"), (button) -> {
            Translations.BANK_CLOSE.send(player);
            this.onClose();
        }));

        this.text.setMaxLength(12);
        this.text.setText("0");
        this.text.setFocusUnlocked(false);
        this.text.setSelected(true);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.children.add(this.text);

    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        images.forEach(image -> image.render(stack, this));
        super.render(stack, mouseX, mouseY, delta);
        this.text.render(stack, mouseX, mouseY, delta);
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        drawCenteredString(stack, this.textRenderer, "Bank Menu - ", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, "Total Money You Have: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, "Total Money in the Bank: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 5, 0xFFFFFF);
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
