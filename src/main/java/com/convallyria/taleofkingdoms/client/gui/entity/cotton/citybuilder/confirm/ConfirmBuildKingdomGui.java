package com.convallyria.taleofkingdoms.client.gui.entity.cotton.citybuilder.confirm;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class ConfirmBuildKingdomGui extends LightweightGuiDescription {

    public ConfirmBuildKingdomGui(PlayerEntity player, CityBuilderEntity entity, ConquestInstance instance) {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(256, 256);

        //todo: translatable
        root.add(new WLabel(Text.literal("Are you sure you want to build here?"), 0xFFFFFF).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), root.getWidth() / 2 - 16, 100, 16, 2);
        root.add(new WLabel(Text.literal("WARNING: When building it will destroy the area around it to make space."), 0xFFFFFF).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), root.getWidth() / 2 - 64, 110, 64, 2);

        WButton buildButton = new WButton(Text.translatable("menu.taleofkingdoms.citybuilder.build"));
        buildButton.setOnClick(() -> {
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
            });
            player.playSound(TaleOfKingdoms.getAPI().getManager(SoundManager.class).getSound(SoundManager.TOKSound.TOKTHEME), SoundCategory.MASTER, 0.1f, 1f);

            // Make city builder stop following player and move to well POI
            TaleOfKingdoms.getAPI().executeOnServer(() -> {
                final CityBuilderEntity cityBuilderServer = (CityBuilderEntity) serverPlayer.world.getEntityById(entity.getId());
                cityBuilderServer.stopFollowingPlayer();
                cityBuilderServer.setTarget(playerKingdom.getPOIPos(KingdomPOI.CITY_BUILDER_WELL_POI));
            });
        });
        root.add(buildButton, root.getWidth() / 2 - 65, root.getHeight() / 2 + 35, 120, 30);

        WButton exitButton = new WButton(Text.literal("Cancel.")).setAlignment(HorizontalAlignment.CENTER);
        exitButton.setOnClick(() -> MinecraftClient.getInstance().currentScreen.close());
        root.add(exitButton, root.getWidth() / 2 - 25, root.getHeight() / 2 + 65, 45, 20);
        root.validate(this);
    }

    @Override
    public void addPainters() {}
}