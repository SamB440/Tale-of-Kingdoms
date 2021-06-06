package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class InnkeeperScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final InnkeeperEntity entity;
    private final ConquestInstance instance;

    public InnkeeperScreen(PlayerEntity player, InnkeeperEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.innkeeper.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.INNKEEPER_REST.send(player);
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 4 + 50, 150, 20, new LiteralText("Rest in a room."), (button) -> {
            this.onClose();
            BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
            if (rest != null) {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    Optional<ConquestInstance> conquestInstance = api.getConquestInstanceStorage().mostRecentInstance();
                    if (conquestInstance.isEmpty()) return;
                    if (conquestInstance.get().getCoins(player.getUuid()) < 10) {
                        return;
                    }

                    MinecraftServer server = MinecraftClient.getInstance().getServer();
                    if (server == null) {
                        api.getClientHandler(TaleOfKingdoms.INNKEEPER_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.INNKEEPER_PACKET_ID,
                                        player,
                                        null, true);
                        return;
                    }

                    api.executeOnServer(() -> {
                        server.getOverworld().setTimeOfDay(1000);
                        ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                        if (serverPlayerEntity == null) return;
                        serverPlayerEntity.refreshPositionAfterTeleport(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                        serverPlayerEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
                        serverPlayerEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                        conquestInstance.get().setCoins(conquestInstance.get().getCoins() - 10);
                    });
                });
            } else {
                player.sendMessage(new LiteralText("House Keeper: It seems there are no rooms available at this time."), false);
            }
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 4 + 75, 150, 20, new LiteralText("Wait for night time."), (button) -> {
            this.onClose();
            MinecraftServer server = MinecraftClient.getInstance().getServer();
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                Optional<ConquestInstance> conquestInstance = api.getConquestInstanceStorage().mostRecentInstance();
                if (conquestInstance.isEmpty()) return;
                if (conquestInstance.get().getCoins(player.getUuid()) < 10) {
                    return;
                }

                if (server == null) {
                    api.getClientHandler(TaleOfKingdoms.INNKEEPER_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.INNKEEPER_PACKET_ID,
                                    player,
                                    null, false);
                    return;
                }

                server.getOverworld().setTimeOfDay(13000);
                conquestInstance.get().setCoins(conquestInstance.get().getCoins() - 10);
            });

        }));

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 4 + 100, 150, 20, new LiteralText("Exit"), (button) -> this.onClose()));
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        drawCenteredString(stack, this.textRenderer, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, "Waiting or resting costs 10 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        Translations.INNKEEPER_LEAVE.send(player);
    }
}
