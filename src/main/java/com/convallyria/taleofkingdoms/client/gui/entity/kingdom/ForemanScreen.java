package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.gui.generic.bar.BarWidget;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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

        //todo: translatable
        rootComponent.child(
            Components.label(Text.literal("Foreman Menu - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"))
                    .color(Color.ofRgb(11111111))
                    .positioning(Positioning.relative(50, 15))
        );

        final int cobbleCount = entity.getInventory().count(Items.COBBLESTONE);
        final float cobblePercent = cobbleCount * (100f / 1280f);

        rootComponent.child(
                Components.label(Text.literal("Resources"))
                        .positioning(Positioning.relative(50, 40))
        );

        rootComponent.child(this.resourcesBar = (BarWidget) new BarWidget(100, 12, cobblePercent / 100)
                .positioning(Positioning.relative(50, 45))
                .tooltip(Text.literal(cobbleCount + " / 1280")));
        
        rootComponent.child(
            Components.button(Text.literal("Collect 64"), c -> {
                final int slotWithStack = InventoryUtils.getSlotWithStack(entity.getInventory(), new ItemStack(Items.COBBLESTONE, 64));
                if (slotWithStack == -1) return;
                player.getInventory().insertStack(entity.getInventory().removeStack(slotWithStack));
                update();
            })
            .sizing(Sizing.fixed(100), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 65))
        );

        rootComponent.child(
            Components.button(Text.literal("Buy Worker."), c -> {
                final int slotWithStack = InventoryUtils.getSlotWithStack(entity.getInventory(), new ItemStack(Items.COBBLESTONE, 64));
                if (slotWithStack == -1) return;
                player.getInventory().insertStack(entity.getInventory().removeStack(slotWithStack));
                update();
            })
            .tooltip(Text.literal("Buying a worker costs 1500 gold."))
            .sizing(Sizing.fixed(100), Sizing.fixed(20))
            .positioning(Positioning.relative(50, 75))
        );

        rootComponent.child(
            Components.button(
                Text.literal("Cancel."),
                (ButtonComponent button) -> this.close()
            ).positioning(Positioning.relative(50, 85))
        );
    }

    private void update() {
        final int cobbleCount = entity.getInventory().count(Items.COBBLESTONE);
        final float cobblePercent = cobbleCount * (100f / 1280f);
        resourcesBar.setBarProgress(cobblePercent / 100);
        resourcesBar.tooltip(Text.literal(cobbleCount + " / 1280"));
    }
}
