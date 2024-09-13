package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.common.packet.c2s.BankerInteractPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.Packets;
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
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(Identifier.of(TaleOfKingdoms.MODID, "banker_ui_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.guildPlayer = instance.getPlayer(player);
        Translations.BANK_OPEN.send(player);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");

        this.totalMoney = inner.childById(LabelComponent.class, "total-money").text(Text.translatable(Translations.BANK_TOTAL_MONEY.getKey(), guildPlayer.getCoins()));
        this.totalMoneyBank = inner.childById(LabelComponent.class, "total-money-bank").text(Text.translatable(Translations.BANK_TOTAL_MONEY_BANK.getKey(), guildPlayer.getBankerCoins()));

        final TextBoxComponent textInput = inner.childById(TextBoxComponent.class, "input-box");

        inner.childById(ButtonComponent.class, "deposit-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }

                if (guildPlayer.getCoins() >= coins) {
                    Translations.BANK_NO_SPEND.send(player);
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdomsClient.getAPI().getClientPacket(Packets.BANKER_INTERACT)
                                .sendPacket(player, new BankerInteractPacket(BankerMethod.DEPOSIT, coins));
                        return;
                    }
                    guildPlayer.setCoins(guildPlayer.getCoins() - coins);
                    guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() + coins);
                }
            } catch (NumberFormatException e) {
                Translations.BANK_INPUT.send(player);
                this.close();
            }
        });

        inner.childById(ButtonComponent.class, "withdraw-button").onPress(b -> {
            try {
                int coins = Integer.parseInt(textInput.getText());
                if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
                    Translations.BANK_ZERO.send(player);
                    this.close();
                    return;
                }
                if (guildPlayer.getBankerCoins() >= coins) {
                    Translations.BANK_THERE.send(player);
                    this.close();
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdomsClient.getAPI().getClientPacket(Packets.BANKER_INTERACT)
                                .sendPacket(player, new BankerInteractPacket(BankerMethod.WITHDRAW, coins));
                        return;
                    }
                    guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() - coins);
                    instance.addCoins(player.getUuid(), coins);
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
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.totalMoney.text(Text.translatable(Translations.BANK_TOTAL_MONEY.getKey(), guildPlayer.getCoins()));
        this.totalMoneyBank.text(Text.translatable(Translations.BANK_TOTAL_MONEY_BANK.getKey(), guildPlayer.getBankerCoins()));
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
