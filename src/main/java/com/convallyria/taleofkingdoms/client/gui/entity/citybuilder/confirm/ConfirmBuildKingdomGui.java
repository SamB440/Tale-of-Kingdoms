package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.confirm;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.BaseCityBuilderScreen;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class ConfirmBuildKingdomGui extends BaseCityBuilderScreen {

    private final PlayerEntity player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public ConfirmBuildKingdomGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        this.player = player;
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        //todo: translatable
        rootComponent.child(
                Components.label(Text.literal("Are you sure you want to build here?"))
                        .positioning(Positioning.relative(50, 45))
        );

        rootComponent.child(
                Components.label(Text.literal("WARNING: When building it will destroy the area around it to make space."))
                        .color(Color.RED)
                        .positioning(Positioning.relative(50, 50))
        );

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.citybuilder.build"), c -> {
                // todo: Will need to send packet to server and verify
                // Close current screen, calculate paste position, and add their kingdom
                MinecraftClient.getInstance().currentScreen.close();
                final IntegratedServer server = MinecraftClient.getInstance().getServer();
                final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
                BlockPos pos = serverPlayer.getBlockPos().subtract(new Vec3i(0, 0, 85));
                final PlayerKingdom playerKingdom = new PlayerKingdom(pos);
                instance.addKingdom(player.getUuid(), playerKingdom);

                // Paste their kingdom
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, serverPlayer, pos).thenAccept(box -> {
                    BlockPos start = new BlockPos(box.getMaxX(), box.getMaxY(), box.getMaxZ());
                    BlockPos end = new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ());
                    playerKingdom.setStart(start);
                    playerKingdom.setEnd(end);

                    // Make city builder stop following player and move to well POI
                    TaleOfKingdoms.getAPI().executeOnServer(() -> {
                        final CityBuilderEntity cityBuilderServer = (CityBuilderEntity) serverPlayer.world.getEntityById(entity.getId());
                        cityBuilderServer.stopFollowingPlayer();
                        // Teleport to the player first, should avoid getting stuck in ground
                        cityBuilderServer.refreshPositionAfterTeleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
                        // Now move to the well location
                        cityBuilderServer.setTarget(playerKingdom.getPOIPos(KingdomPOI.CITY_BUILDER_WELL_POI));
                    });
                });
                player.playSound(TaleOfKingdoms.getAPI().getManager(SoundManager.class).getSound(SoundManager.TOKSound.TOKTHEME), SoundCategory.MASTER, 0.1f, 1f);
            })
            .positioning(Positioning.relative(50, 67))
        );

        rootComponent.child(
            Components.button(
                Text.literal("Cancel."),
                (ButtonComponent button) -> this.close()
            )
            .positioning(Positioning.relative(50, 75))
        );
    }
}