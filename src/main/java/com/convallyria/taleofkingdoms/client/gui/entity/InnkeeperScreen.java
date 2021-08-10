package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class InnkeeperScreen extends ScreenTOK {

    private final Player player;
    private final InnkeeperEntity entity;
    private final ConquestInstance instance;

    public InnkeeperScreen(Player player, InnkeeperEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.innkeeper.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.INNKEEPER_REST.send(player);
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 4 + 50, 150, 20, new TextComponent("Rest in a room."), (button) -> {
            this.onClose();
            BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
            if (rest != null) {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    Optional<ConquestInstance> conquestInstance = api.getConquestInstanceStorage().mostRecentInstance();
                    if (conquestInstance.isEmpty()) return;
                    if (conquestInstance.get().getCoins(player.getUUID()) < 10) {
                        return;
                    }

                    MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
                    if (server == null) {
                        api.getClientHandler(TaleOfKingdoms.INNKEEPER_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.INNKEEPER_PACKET_ID,
                                        player,
                                        null, true);
                        return;
                    }

                    api.executeOnServer(() -> {
                        server.overworld().setDayTime(1000);
                        ServerPlayer serverPlayerEntity = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
                        if (serverPlayerEntity == null) return;
                        serverPlayerEntity.moveTo(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                        serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
                        serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                        conquestInstance.get().setCoins(conquestInstance.get().getCoins() - 10);
                    });
                });
            } else {
                player.displayClientMessage(new TextComponent("House Keeper: It seems there are no rooms available at this time."), false);
            }
        }));

        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 4 + 75, 150, 20, new TextComponent("Wait for night time."), (button) -> {
            this.onClose();
            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                Optional<ConquestInstance> conquestInstance = api.getConquestInstanceStorage().mostRecentInstance();
                if (conquestInstance.isEmpty()) return;
                if (conquestInstance.get().getCoins(player.getUUID()) < 10) {
                    return;
                }

                if (server == null) {
                    api.getClientHandler(TaleOfKingdoms.INNKEEPER_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.INNKEEPER_PACKET_ID,
                                    player,
                                    null, false);
                    return;
                }

                server.overworld().setDayTime(13000);
                conquestInstance.get().setCoins(conquestInstance.get().getCoins() - 10);
            });

        }));

        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 4 + 100, 150, 20, new TextComponent("Exit"), (button) -> this.onClose()));
    }

    @Override
    public void render(PoseStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        drawCenteredString(stack, this.font, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.font, "Waiting or resting costs 10 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
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
