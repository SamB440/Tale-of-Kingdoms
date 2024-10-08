package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.confirm;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.BaseCityBuilderScreen;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuildKingdomPacket;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class ConfirmBuildKingdomGui extends BaseCityBuilderScreen {

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public ConfirmBuildKingdomGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        super(DataSource.asset(Identifier.of(TaleOfKingdoms.MODID, "citybuilder_confirm_build_kingdom_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.citybuilder.build"), c -> {
                // Close current screen, calculate paste position, and add their kingdom
                MinecraftClient.getInstance().currentScreen.close();
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.BUILD_KINGDOM)
                            .sendPacket(player, new BuildKingdomPacket(entity.getId()));
                    return;
                }

                final IntegratedServer server = MinecraftClient.getInstance().getServer();
                final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
                BlockPos pos = serverPlayer.getBlockPos().subtract(new Vec3i(0, 25, 85));
                final PlayerKingdom playerKingdom = new PlayerKingdom(pos);
                final GuildPlayer guildPlayer = instance.getPlayer(player);
                guildPlayer.setKingdom(playerKingdom);

                // Paste their kingdom
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, serverPlayer, pos).thenAccept(box -> {
                    BlockPos start = new BlockPos(box.getMaxX(), box.getMaxY(), box.getMaxZ());
                    BlockPos end = new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ());
                    playerKingdom.setStart(start);
                    playerKingdom.setEnd(end);

                    // Make city builder stop following player and move to well POI
                    TaleOfKingdoms.getAPI().executeOnServerEnvironment((s) -> {
                        final CityBuilderEntity cityBuilderServer = (CityBuilderEntity) serverPlayer.getWorld().getEntityById(entity.getId());
                        cityBuilderServer.stopFollowingPlayer();
                        // Teleport to the player first, should avoid getting stuck in ground
                        cityBuilderServer.requestTeleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
                        // Now move to the well location
                        cityBuilderServer.setTarget(playerKingdom.getPOIPos(KingdomPOI.CITY_BUILDER_WELL_POI));
                    });
                });
                player.playSoundToPlayer(TaleOfKingdoms.getAPI().getManager(SoundManager.class).getSound(SoundManager.TOKSound.TOKTHEME), SoundCategory.MUSIC, 0.1f, 1f);
            })
            .positioning(Positioning.relative(50, 67))
        );

        rootComponent.child(
            Components.button(
                Text.translatable("menu.taleofkingdoms.generic.cancel"),
                (ButtonComponent button) -> this.close()
            )
            .positioning(Positioning.relative(50, 75))
        );
    }
}