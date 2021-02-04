package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class BankerScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final BankerEntity entity;
    private final ClientConquestInstance instance;

    // Text fields
    private TextFieldWidget text;

    public BankerScreen(PlayerEntity player, BankerEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.banker.name");
        this.player = player;
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), this.width / 2 + 50, this.height / 2 + 25, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
        Translations.BANK_OPEN.send(player);
    }

    @Override
    public void init() {
        super.init();
        this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 77, this.height / 2 - 85, 150, 20, new LiteralText("0"));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 - 20, 150, 20, new LiteralText("Deposit"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getText());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.onClose();
                    return;
                }

                if (instance.getCoins() >= coins) {
                    instance.setCoins(instance.getCoins() - coins);
                    instance.setBankerCoins(instance.getBankerCoins() + coins);
                    this.onClose();
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.onClose();
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 5, 150, 20, new LiteralText("Withdraw"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getText());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.onClose();
                    return;
                }
                if (instance.getBankerCoins() >= coins) {
                    instance.setBankerCoins(instance.getBankerCoins() - coins);
                    instance.addCoins(coins);
                    this.onClose();
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.onClose();
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 30, 150, 20, new LiteralText("Exit"), (button) -> {
            this.onClose();
        }));

        this.text.setMaxLength(12);
        this.text.setText("0");
        this.text.setFocusUnlocked(false);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.children.add(this.text);

    }

    @Override
    public void onClose() {
        super.onClose();
        Translations.BANK_NO_SPEND.send(player);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        this.text.render(stack, mouseX, mouseY, delta);
        drawCenteredString(stack, this.textRenderer, "Bank Menu - ", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, "Total Money You Have: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, "Total Money in the Bank: " + instance.getBankerCoins() + " Gold Coins", this.width / 2, this.height / 4 - 5, 0xFFFFFF);
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
