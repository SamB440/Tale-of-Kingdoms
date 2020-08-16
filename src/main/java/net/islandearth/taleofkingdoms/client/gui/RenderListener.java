package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.InventoryDrawCallback;
import net.islandearth.taleofkingdoms.common.listener.Listener;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;

import java.util.Optional;

public class RenderListener extends Listener {

	public RenderListener() {
		InventoryDrawCallback.EVENT.register((gui, matrices) -> {
			String worldName = MinecraftClient.getInstance().getServer().getRunDirectory().getName();
			Optional<ConquestInstance> instance = TaleOfKingdoms
					.getAPI()
					.get()
					.getConquestInstanceStorage()
					.getConquestInstance(worldName);
			if (!instance.isPresent()) return;
			gui.drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, "Gold Coins: " + instance.get().getCoins(), gui.width / 2 - 60, gui.height / 2 - 100, 16763904);
		});
	}
}
