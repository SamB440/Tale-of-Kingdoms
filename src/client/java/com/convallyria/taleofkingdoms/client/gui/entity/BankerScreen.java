package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BankerInteractPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BankerScreen extends BaseUIModelScreen<FlowLayout> {

    private final PlayerEntity player;
    private final BankerEntity entity;
    private final ConquestInstance instance;
    private final GuildPlayer guildPlayer;

    private LabelComponent totalMoney, totalMoneyBank;

    public BankerScreen(PlayerEntity player, BankerEntity entity, ConquestInstance instance) {
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(
                Identifier.of(TaleOfKingdoms.MODID, "banker_ui_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.guildPlayer = instance.getPlayer(player);
        Translations.BANK_OPEN.send(player);
    }

    @Override
    protected void build(FlowLayout rootComponent) {

        this.totalMoney = rootComponent.childById(LabelComponent.class, "total-money")
                .text(Text.translatable(Translations.BANK_TOTAL_MONEY.getKey(), guildPlayer.getCoins()));
        this.totalMoneyBank = rootComponent.childById(LabelComponent.class, "total-money-bank")
                .text(Text.translatable(Translations.BANK_TOTAL_MONEY_BANK.getKey(), guildPlayer.getBankerCoins()));

        final TextBoxComponent textInput = rootComponent.childById(TextBoxComponent.class, "input-box");

        rootComponent.childById(ButtonComponent.class, "deposit-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                handleTransaction(coins, BankerMethod.DEPOSIT);
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        });

        rootComponent.childById(ButtonComponent.class, "withdraw-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                handleTransaction(coins, BankerMethod.WITHDRAW);
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        });

        rootComponent.childById(ButtonComponent.class, "percent-20-button-you").onPress(b -> {
            int amount = (int) (guildPlayer.getCoins() * 0.20);
            handleTransaction(amount, BankerMethod.DEPOSIT);
        });

        rootComponent.childById(ButtonComponent.class, "percent-50-button-you").onPress(b -> {
            int amount = (int) (guildPlayer.getCoins() * 0.50);
            handleTransaction(amount, BankerMethod.DEPOSIT);
        });

        rootComponent.childById(ButtonComponent.class, "percent-100-button-you").onPress(b -> {
            int amount = guildPlayer.getCoins();
            handleTransaction(amount, BankerMethod.DEPOSIT);
        });

        rootComponent.childById(ButtonComponent.class, "percent-20-button-bank").onPress(b -> {
            int amount = (int) (guildPlayer.getBankerCoins() * 0.20);
            handleTransaction(amount, BankerMethod.WITHDRAW);
        });

        rootComponent.childById(ButtonComponent.class, "percent-50-button-bank").onPress(b -> {
            int amount = (int) (guildPlayer.getBankerCoins() * 0.50);
            handleTransaction(amount, BankerMethod.WITHDRAW);
        });

        rootComponent.childById(ButtonComponent.class, "percent-100-button-bank").onPress(b -> {
            int amount = guildPlayer.getBankerCoins();
            handleTransaction(amount, BankerMethod.WITHDRAW);
        });

        rootComponent.childById(ButtonComponent.class, "exit-button").onPress(b -> this.close());
    }

    private void handleTransaction(int coins, BankerMethod method) {
        if (coins <= 0) {
            Translations.BANK_ZERO.send(player);
            return;
        }

        if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
            Translations.BANK_ZERO.send(player);
            return;
        }

        if (method == BankerMethod.DEPOSIT) {
            if (guildPlayer.getCoins() >= coins) {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.BANKER_INTERACT)
                            .sendPacket(player, new BankerInteractPacket(method, coins));
                } else {
                    guildPlayer.setCoins(guildPlayer.getCoins() - coins);
                    guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() + coins);
                }
                Translations.BANK_NO_SPEND.send(player);
            } else {
                Translations.BANK_ZERO.send(player);
            }
        } else if (method == BankerMethod.WITHDRAW) {
            if (guildPlayer.getBankerCoins() >= coins) {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.BANKER_INTERACT)
                            .sendPacket(player, new BankerInteractPacket(method, coins));
                } else {
                    guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() - coins);
                    instance.addCoins(player.getUuid(), coins);
                }
            } else {
                Translations.BANK_THERE.send(player);
            }
        }
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.totalMoney.text(Text.translatable(
                Translations.BANK_TOTAL_MONEY.getKey(), guildPlayer.getCoins()));
        this.totalMoneyBank.text(Text.translatable(
                Translations.BANK_TOTAL_MONEY_BANK.getKey(), guildPlayer.getBankerCoins()));
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
