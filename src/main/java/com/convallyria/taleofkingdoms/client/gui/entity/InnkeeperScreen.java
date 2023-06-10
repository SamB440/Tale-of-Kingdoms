package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

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
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.taleofkingdoms.innkeeper.rest"), widget -> {
            this.close();
            BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
            if (rest != null) {
                final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
                ConquestInstance conquestInstance = api.getConquestInstanceStorage().mostRecentInstance().orElse(null);
                if (conquestInstance == null) return;

                final GuildPlayer guildPlayer = conquestInstance.getPlayer(player.getUuid());
                if (guildPlayer.getCoins() < 10) {
                    return;
                }

                MinecraftServer server = MinecraftClient.getInstance().getServer();
                if (server == null) {
                    api.getClientPacketHandler(Packets.INNKEEPER_HIRE_ROOM)
                            .handleOutgoingPacket(player, true);
                    return;
                }

                api.executeOnServer(() -> {
                    server.getOverworld().setTimeOfDay(1000);
                    ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity == null) return;
                    serverPlayerEntity.refreshPositionAfterTeleport(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    guildPlayer.setCoins(guildPlayer.getCoins() - 10);
                });
            } else {
                player.sendMessage(Text.translatable("menu.taleofkingdoms.innkeeper.no_rooms"));
            }
        }).dimensions(this.width / 2 - 75, this.height / 4 + 50, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.taleofkingdoms.innkeeper.wait"), widget -> {
            this.close();
            MinecraftServer server = MinecraftClient.getInstance().getServer();
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            ConquestInstance conquestInstance = api.getConquestInstanceStorage().mostRecentInstance().orElse(null);
            if (conquestInstance == null) return;

            final GuildPlayer guildPlayer = conquestInstance.getPlayer(player.getUuid());
            if (guildPlayer.getCoins() < 10) {
                return;
            }

            if (server == null) {
                api.getClientPacketHandler(Packets.INNKEEPER_HIRE_ROOM)
                        .handleOutgoingPacket(player, false);
                return;
            }

            server.getOverworld().setTimeOfDay(13000);
            guildPlayer.setCoins(guildPlayer.getCoins() - 10);
        }).dimensions(this.width / 2 - 75, this.height / 4 + 75, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.taleofkingdoms.generic.exit"), widget -> {
            this.close();
        }).dimensions(this.width / 2 - 75, this.height / 4 + 100, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int par1, int par2, float par3) {
        super.render(context, par1, par2, par3);
        context.drawCenteredTextWithShadow(this.textRenderer, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Waiting or resting costs 10 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        super.close();
        Translations.INNKEEPER_LEAVE.send(player);
    }
}
