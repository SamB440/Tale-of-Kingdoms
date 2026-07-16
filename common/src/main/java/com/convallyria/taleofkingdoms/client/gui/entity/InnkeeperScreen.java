package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.InnkeeperActionPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class InnkeeperScreen extends ScreenTOK {

    private final Player player;
    private final InnkeeperEntity entity;
    private final ConquestInstance instance;

    public InnkeeperScreen(Player player, InnkeeperEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.innkeeper.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.INNKEEPER_REST.send(player, PlayerTitle.of(player).getAddress());
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.innkeeper.rest"), widget -> {
            this.onClose();
            BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
            if (rest != null) {
                final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
                ConquestInstance conquestInstance = api.getConquestInstanceStorage().mostRecentInstance().orElse(null);
                if (conquestInstance == null) return;

                final GuildPlayer guildPlayer = conquestInstance.getPlayer(player.getUUID());
                if (guildPlayer.getCoins() < 10) {
                    Translations.INNKEEPER_NOT_ENOUGH_COINS.send(player);
                    return;
                } else {
                    Translations.INNKEEPER_REST_SUCCESS.send(player);
                }

                MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
                if (server == null) {
                    api.getClientPacket(Packets.INNKEEPER_HIRE_ROOM)
                            .sendPacket(player, new InnkeeperActionPacket(true));
                    return;
                }

                api.executeOnServerEnvironment((s) -> {
                    adjustTime(server, 1000);
                    ServerPlayer serverPlayerEntity = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
                    if (serverPlayerEntity == null) return;
                    serverPlayerEntity.teleportTo(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    serverPlayerEntity.moveTo(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
                    serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                    guildPlayer.setCoins(guildPlayer.getCoins() - 10);
                });
            } else {
                player.sendSystemMessage(Component.translatable("menu.taleofkingdoms.innkeeper.no_rooms"));
            }
        }).bounds(this.width / 2 - 75, this.height / 4 + 50, 150, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.innkeeper.wait"), widget -> {
            this.onClose();
            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            ConquestInstance conquestInstance = api.getConquestInstanceStorage().mostRecentInstance().orElse(null);
            if (conquestInstance == null) return;

            final GuildPlayer guildPlayer = conquestInstance.getPlayer(player.getUUID());
            if (guildPlayer.getCoins() < 10) {
                Translations.INNKEEPER_NOT_ENOUGH_COINS.send(player);
                return;
            } else {
                Translations.INNKEEPER_WAIT_SUCCESS.send(player);
            }

            if (server == null) {
                api.getClientPacket(Packets.INNKEEPER_HIRE_ROOM)
                        .sendPacket(player, new InnkeeperActionPacket(false));
                return;
            }

            adjustTime(server, 13000);
            guildPlayer.setCoins(guildPlayer.getCoins() - 10);
        }).bounds(this.width / 2 - 75, this.height / 4 + 75, 150, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.generic.exit"), widget -> {
            this.onClose();
            Translations.INNKEEPER_LEAVE.send(player);
        }).bounds(this.width / 2 - 75, this.height / 4 + 100, 150, 20).build());
    }

    private void adjustTime(MinecraftServer server, long targetTime) {
        long currentTime = server.overworld().getDayTime() % 24000;
        long newTime = (currentTime < targetTime) ? targetTime : 24000 + targetTime;
        server.overworld().setDayTime(server.overworld().getDayTime() + (newTime - currentTime));
    }

    @Override
    public void render(GuiGraphics context, int par1, int par2, float par3) {
        super.render(context, par1, par2, par3);
        context.drawCenteredString(this.font, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        context.drawCenteredString(this.font, "Waiting or resting costs 10 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
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
