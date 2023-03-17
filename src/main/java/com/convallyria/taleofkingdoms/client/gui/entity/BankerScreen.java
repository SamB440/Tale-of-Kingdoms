package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class BankerScreen extends BaseUIModelScreen<FlowLayout> {

    private final PlayerEntity player;
    private final BankerEntity entity;
    private final ConquestInstance instance;

    private LabelComponent totalMoney, totalMoneyBank;

    public BankerScreen(PlayerEntity player, BankerEntity entity, ConquestInstance instance) {
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(new Identifier(TaleOfKingdoms.MODID, "banker_ui_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.BANK_OPEN.send(player);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        final UUID playerUuid = player.getUuid();
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");

        this.totalMoney = inner.childById(LabelComponent.class, "total-money").text(Text.translatable(Translations.BANK_TOTAL_MONEY.getKey(), instance.getCoins(player.getUuid())));
        this.totalMoneyBank = inner.childById(LabelComponent.class, "total-money-bank").text(Text.translatable(Translations.BANK_TOTAL_MONEY_BANK.getKey(), instance.getBankerCoins(player.getUuid())));

        final TextBoxComponent textInput = inner.childById(TextBoxComponent.class, "input-box");

        inner.childById(ButtonComponent.class, "deposit-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                if (instance.getCoins(playerUuid) == 0 && instance.getBankerCoins(playerUuid) == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }

                if (instance.getCoins(playerUuid) >= coins) {
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player, BankerMethod.DEPOSIT, coins);
                        return;
                    }
                    instance.setCoins(playerUuid, instance.getCoins(playerUuid) - coins);
                    instance.setBankerCoins(playerUuid, instance.getBankerCoins(playerUuid) + coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        });

        inner.childById(ButtonComponent.class, "withdraw-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                if (instance.getCoins(playerUuid) == 0 && instance.getBankerCoins(playerUuid) == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }
                if (instance.getBankerCoins(playerUuid) >= coins) {
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID,
                                        player, BankerMethod.WITHDRAW, coins);
                        return;
                    }
                    instance.setBankerCoins(playerUuid, instance.getBankerCoins(playerUuid) - coins);
                    instance.addCoins(playerUuid, coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        });

        inner.childById(ButtonComponent.class, "exit-button").onPress(b -> this.close());
    }

    @Override
    public void close() {
        super.close();
        Translations.BANK_NO_SPEND.send(player);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        this.totalMoney.text(Text.translatable(Translations.BANK_TOTAL_MONEY.getKey(), instance.getCoins(player.getUuid())));
        this.totalMoneyBank.text(Text.translatable(Translations.BANK_TOTAL_MONEY_BANK.getKey(), instance.getBankerCoins(player.getUuid())));
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
