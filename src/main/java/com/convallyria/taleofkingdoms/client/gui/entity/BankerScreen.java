package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.schematic.ClientConquestInstance;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class BankerScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final BankerEntity entity;
    private final ClientConquestInstance instance;

    // Text fields
    private TextFieldWidget text;

    public BankerScreen(PlayerEntity player, BankerEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.banker.name");
        this.player = player;
        //addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), this.width / 2 + 50, this.height / 2 + 25, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
        Translations.BANK_OPEN.send(player);
    }

    @Override
    public void init() {
        super.init();
        this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 77, this.height / 2 - 85, 150, 20, Text.literal("0"));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 77, this.height / 2 - 20, 150, 20, Text.literal("Deposit"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getText());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }

                if (instance.getCoins() >= coins) {
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player, BankerMethod.DEPOSIT, coins);
                        return;
                    }
                    instance.setCoins(instance.getCoins() - coins);
                    instance.setBankerCoins(instance.getBankerCoins() + coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 5, 150, 20, Text.literal("Withdraw"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getText());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }
                if (instance.getBankerCoins() >= coins) {
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player, BankerMethod.WITHDRAW, coins);
                        return;
                    }
                    instance.setBankerCoins(instance.getBankerCoins() - coins);
                    instance.addCoins(coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 77, this.height / 2 + 30, 150, 20, Text.literal("Exit"), (button) -> {
            this.close();
        }));

        this.text.setMaxLength(12);
        this.text.setText("0");
        this.text.setTextFieldFocused(true);
        this.text.setFocusUnlocked(true);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.text.setCursorToEnd();
        this.addSelectableChild(this.text);
    }

    @Override
    public void close() {
        super.close();
        Translations.BANK_NO_SPEND.send(player);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        this.text.render(stack, mouseX, mouseY, delta);
        drawCenteredText(stack, this.textRenderer, "Bank Menu - ", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, "Total Money You Have: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, "Total Money in the Bank: " + instance.getBankerCoins() + " Gold Coins", this.width / 2, this.height / 4 - 5, 0xFFFFFF);
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
