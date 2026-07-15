package com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.confirm;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.BaseCityBuilderScreen;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.KingdomTier;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.UpgradeKingdomPacket;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class ConfirmUpgradeKingdomGui extends BaseCityBuilderScreen {

    private final Player player;
    private final CityBuilderEntity entity;
    private final ConquestInstance instance;

    public ConfirmUpgradeKingdomGui(Player player, CityBuilderEntity entity, ConquestInstance instance) {
        super(DataSource.asset(ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "citybuilder_confirm_upgrade_kingdom_model")));
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

        rootComponent.child(
                Components.label(Component.translatable("menu.taleofkingdoms.citybuilder.build_confirm"))
                        .positioning(Positioning.relative(50, 45))
        );

        rootComponent.child(
                Components.label(Component.translatable("menu.taleofkingdoms.citybuilder.destroy"))
                        .color(Color.RED)
                        .positioning(Positioning.relative(50, 50))
        );

        rootComponent.child(
                Components.label(Component.translatable("menu.taleofkingdoms.citybuilder.destroy_items"))
                        .color(Color.RED)
                        .positioning(Positioning.relative(50, 55))
        );

        rootComponent.child(
            Components.button(Component.translatable("menu.taleofkingdoms.citybuilder.upgrade"), c -> {
                // Close current screen, calculate paste position, and upgrade their kingdom
                Minecraft.getInstance().screen.onClose();
                if (Minecraft.getInstance().getSingleplayerServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.UPGRADE_KINGDOM)
                            .sendPacket(player, new UpgradeKingdomPacket(entity.getId()));
                    return;
                }

                entity.getInventory().removeItemType(Items.OAK_LOG, 320);
                entity.getInventory().removeItemType(Items.COBBLESTONE, 320);

                final IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
                final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
                final PlayerKingdom kingdom = instance.getPlayer(player).getKingdom();
                final BlockPos origin = kingdom.getOrigin();

                // Paste their kingdom
                final KingdomTier next = KingdomTier.values()[kingdom.getTier().ordinal() + 1];
                kingdom.setTier(next);

                // Tier 2 has +49 blocks on z axis
                // +15 on x
                final BlockPos offsetPos = origin.subtract(next.getOffset());
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(next.getSchematic(), serverPlayer, offsetPos).thenAccept(box -> {
                    BlockPos start = new BlockPos(box.maxX(), box.maxY(), box.maxZ());
                    BlockPos end = new BlockPos(box.minX(), box.minY(), box.minZ());
                    kingdom.setStart(start);
                    kingdom.setEnd(end);
                });
                player.playNotifySound(TaleOfKingdoms.getAPI().getManager(SoundManager.class).getSound(SoundManager.TOKSound.TOKTHEME), SoundSource.MUSIC, 0.1f, 1f);
            })
            .positioning(Positioning.relative(50, 67))
        );

        rootComponent.child(
            Components.button(
                Component.translatable("menu.taleofkingdoms.generic.cancel"),
                (ButtonComponent button) -> this.onClose()
            )
            .positioning(Positioning.relative(50, 75))
        );
    }
}