package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.common.entity.kingdom.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class QuarryForemanGui extends LightweightGuiDescription {

    private final QuarryForemanEntity entity;
    private final WLabel cobbleLabel;

    public QuarryForemanGui(PlayerEntity player, QuarryForemanEntity entity, ConquestInstance instance) {
        this.entity = entity;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(400, 256);

        //todo: translatable
        root.add(new WLabel(Text.literal("Foreman Menu - Total Money: " + instance.getCoins(player.getUuid()) + " Gold Coins"), 11111111).setVerticalAlignment(VerticalAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER), root.getWidth() / 2 - 16, 10, 32, 2);

        root.add(new WLabel(Text.literal("Resources"), WLabel.DEFAULT_DARKMODE_TEXT_COLOR).setHorizontalAlignment(HorizontalAlignment.CENTER), root.getWidth() / 2 - 10, root.getHeight() / 2 - 8, 10, 2);

        final int cobbleCount = entity.getInventory().count(Items.COBBLESTONE);
        this.cobbleLabel = new WLabel(Text.literal(cobbleCount + " / 1280"), WLabel.DEFAULT_DARKMODE_TEXT_COLOR).setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(cobbleLabel, root.getWidth() / 2 - 12, root.getHeight() / 2, 12, 2);
        WButton collect64 = new WButton(Text.literal("Collect 64"));
        collect64.setOnClick(() -> {
            final int slotWithStack = InventoryUtils.getSlotWithStack(entity.getInventory(), new ItemStack(Items.COBBLESTONE, 64));
            if (slotWithStack == -1) return;
            player.getInventory().insertStack(entity.getInventory().removeStack(slotWithStack));
            update();
        });
        collect64.setAlignment(HorizontalAlignment.CENTER);
        root.add(collect64, root.getWidth() / 2 - 60, root.getHeight() / 2 + 35, 120, 30);

        WButton buyWorker = new WButton(Text.literal("Buy Worker."));
        buyWorker.setOnClick(() -> {
            final int coins = instance.getCoins(player.getUuid());
            if (coins >= 1500) {
                instance.setCoins(player.getUuid(), coins - 1500);
                //TODO: spawn worker
            }
        });
        buyWorker.addTooltip(new TooltipBuilder().add(Text.literal("Buying a worker costs 1500 gold.")));
        buyWorker.setAlignment(HorizontalAlignment.CENTER);
        root.add(buyWorker, root.getWidth() / 2 - 60, root.getHeight() / 2 + 65, 120, 30);

        WButton exitButton = new WButton(Text.literal("Cancel."));
        exitButton.setOnClick(() -> MinecraftClient.getInstance().currentScreen.close());
        root.add(exitButton, root.getWidth() / 2 - 22, root.getHeight() / 2 + 95, 45, 20);
        exitButton.setAlignment(HorizontalAlignment.CENTER);
        root.validate(this);
    }

    private void update() {
        final int cobbleCount = entity.getInventory().count(Items.COBBLESTONE);
        cobbleLabel.setText(Text.literal(cobbleCount + " / 1280"));
    }

    @Override
    public void addPainters() {}
}