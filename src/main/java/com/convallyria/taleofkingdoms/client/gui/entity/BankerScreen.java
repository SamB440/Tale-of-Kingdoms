package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class BankerScreen extends ScreenTOK {

    private final Player player;
    private final BankerEntity entity;
    private final ClientConquestInstance instance;

    // Text fields
    private EditBox text;

    public BankerScreen(Player player, BankerEntity entity, ClientConquestInstance instance) {
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
        this.text = new EditBox(this.font, this.width / 2 - 77, this.height / 2 - 85, 150, 20, new TextComponent("0"));
        this.addRenderableWidget(new Button(this.width / 2 - 77, this.height / 2 - 20, 150, 20, new TextComponent("Deposit"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getValue());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.onClose();
                    return;
                }

                if (instance.getCoins() >= coins) {
                    this.onClose();
                    if (Minecraft.getInstance().getSingleplayerServer() == null) {
                        TaleOfKingdoms.getAPI().get().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player,
                                        null, BankerMethod.DEPOSIT, coins);
                        return;
                    }
                    instance.setCoins(instance.getCoins() - coins);
                    instance.setBankerCoins(instance.getBankerCoins() + coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.onClose();
            }
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 77, this.height / 2 + 5, 150, 20, new TextComponent("Withdraw"), (button) -> {
            try {
                int coins = Integer.parseInt(this.text.getValue());
                if (instance.getCoins() == 0 && instance.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.onClose();
                    return;
                }
                if (instance.getBankerCoins() >= coins) {
                    this.onClose();
                    if (Minecraft.getInstance().getSingleplayerServer() == null) {
                        TaleOfKingdoms.getAPI().get().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player,
                                        null, BankerMethod.WITHDRAW, coins);
                        return;
                    }
                    instance.setBankerCoins(instance.getBankerCoins() - coins);
                    instance.addCoins(coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.onClose();
            }
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 77, this.height / 2 + 30, 150, 20, new TextComponent("Exit"), (button) -> {
            this.onClose();
        }));

        this.text.setMaxLength(12);
        this.text.setValue("0");
        this.text.setFocus(true);
        this.text.setCanLoseFocus(true);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.text.moveCursorToEnd();
        this.addWidget(this.text);
    }

    @Override
    public void onClose() {
        super.onClose();
        Translations.BANK_NO_SPEND.send(player);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        this.text.render(stack, mouseX, mouseY, delta);
        drawCenteredString(stack, this.font, "Bank Menu - ", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.font, "Total Money You Have: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 15, 0xFFFFFF);
        drawCenteredString(stack, this.font, "Total Money in the Bank: " + instance.getBankerCoins() + " Gold Coins", this.width / 2, this.height / 4 - 5, 0xFFFFFF);
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
