package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.WardenEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

public class WardenScreen extends BaseOwoScreen<FlowLayout> {

    private final PlayerEntity player;
    private final WardenEntity entity;
    private final ConquestInstance instance;

    public WardenScreen(PlayerEntity player, WardenEntity entity, ConquestInstance instance) {
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        player.sendMessage(Text.translatable("entity_type.taleofkingdoms.warden.gui.open"));
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::horizontalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(
            Components.label(Text.translatable("menu.taleofkingdoms.warden.total_money", instance.getPlayer(player).getCoins()))
                    .color(Color.ofDye(DyeColor.WHITE))
                    .positioning(Positioning.relative(50, 15))
        );

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.warden.recruit.knight"), c -> {
                entity.buySoldier(player, instance, (byte) 1);
            })
            .tooltip(Text.translatable("menu.taleofkingdoms.warden.buy_cost"))
            .sizing(Sizing.content(), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 45))
        );

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.warden.recruit.archer"), c -> {
                entity.buySoldier(player, instance, (byte) 2);
//                if (MinecraftClient.getInstance().getServer() == null) {
//                    TaleOfKingdomsClient.getAPI().getClientPacketHandler(Packets.FOREMAN_BUY_WORKER)
//                            .handleOutgoingPacket(player, entity.getId());
//                    return;
//                }
//
//                entity.buyWorker(player, instance);
            })
            .tooltip(Text.translatable("menu.taleofkingdoms.warden.buy_cost"))
            .sizing(Sizing.content(), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 55))
        );

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.warden.recall"), c -> {
//                if (MinecraftClient.getInstance().getServer() == null) {
//                    TaleOfKingdomsClient.getAPI().getClientPacketHandler(Packets.FOREMAN_BUY_WORKER)
//                            .handleOutgoingPacket(player, entity.getId());
//                    return;
//                }
//
                entity.recallSoldiers(player, instance);
            })
            .sizing(Sizing.content(), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 65))
        );

        rootComponent.child(
            Components.button(
                Text.translatable("menu.taleofkingdoms.generic.exit"),
                (ButtonComponent button) -> this.close()
            ).positioning(Positioning.relative(50, 85))
        );
    }

    @Override
    public void close() {
        super.close();
        player.sendMessage(Text.translatable("entity_type.taleofkingdoms.warden.gui.close"));
    }
}
