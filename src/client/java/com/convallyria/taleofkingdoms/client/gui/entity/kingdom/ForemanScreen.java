package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.generic.bar.BarWidget;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanBuyWorkerPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanCollectPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

public class ForemanScreen extends BaseOwoScreen<FlowLayout> {

    private final PlayerEntity player;
    private final ForemanEntity entity;
    private final ConquestInstance instance;
    private BarWidget resourcesBar;

    public ForemanScreen(PlayerEntity player, ForemanEntity entity, ConquestInstance instance) {
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.FOREMAN_NEED_RESOURCES.send(player);
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
            Components.label(Text.translatable("menu.taleofkingdoms.foreman.total_money", instance.getPlayer(player).getCoins()))
                    .color(Color.ofDye(DyeColor.GRAY))
                    .positioning(Positioning.relative(50, 15))
        );

        final Item item = entity instanceof QuarryForemanEntity ? Items.COBBLESTONE : Items.OAK_LOG;
        final int resourceCount = entity instanceof QuarryForemanEntity ? entity.getStone() : entity.getWood();
        final float resourcePercent = resourceCount * (100f / 1280f);

        rootComponent.child(
                Components.label(Text.translatable("menu.taleofkingdoms.foreman.resources"))
                        .positioning(Positioning.relative(50, 40))
        );

        rootComponent.child(this.resourcesBar = (BarWidget) new BarWidget(100, 12, resourcePercent / 100)
                .positioning(Positioning.relative(50, 45))
                .tooltip(Text.literal(resourceCount + " / 1280")));

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.foreman.collect"), c -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.FOREMAN_COLLECT)
                            .sendPacket(player, new ForemanCollectPacket(entity.getId()));
                    return;
                }

                entity.collect64(player, item);
            })
            .sizing(Sizing.fixed(100), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 65))
        );

        rootComponent.child(
            Components.button(Text.translatable("menu.taleofkingdoms.foreman.buy_worker"), c -> {
                if (MinecraftClient.getInstance().getServer() == null) {
                    TaleOfKingdomsClient.getAPI().getClientPacket(Packets.FOREMAN_BUY_WORKER)
                            .sendPacket(player, new ForemanBuyWorkerPacket(entity.getId()));
                    return;
                }

                entity.buyWorker(player, instance);
            })
            .tooltip(Text.translatable("menu.taleofkingdoms.foreman.buy_worker_cost"))
            .sizing(Sizing.fixed(100), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 75))
        );

        rootComponent.child(
            Components.button(
                Text.translatable("menu.taleofkingdoms.generic.cancel"),
                (ButtonComponent button) -> this.close()
            ).positioning(Positioning.relative(50, 85))
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        update();
    }

    private void update() {
        final int resourceCount = entity instanceof QuarryForemanEntity ? entity.getStone() : entity.getWood();
        final float resourcePercent = resourceCount * (100f / 1280f);
        resourcesBar.setBarProgress(resourcePercent / 100);
        resourcesBar.tooltip(Text.literal(resourceCount + " / 1280"));
    }
}
