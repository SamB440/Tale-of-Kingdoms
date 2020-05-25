package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class RenderListener extends Listener {

	@SubscribeEvent
	public void onRender(GuiScreenEvent.DrawScreenEvent.Post e) {
		if (e.getGui() instanceof InventoryScreen) {
			InventoryScreen gui = (InventoryScreen) e.getGui();
			String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
			Optional<ConquestInstance> instance = TaleOfKingdoms
					.getAPI()
					.get()
					.getConquestInstanceStorage()
					.getConquestInstance(worldName);
			if (!instance.isPresent()) return;
			gui.drawString(gui.getMinecraft().fontRenderer, "Gold Coins: " + instance.get().getCoins(), gui.width / 2 - 60, gui.height / 2 - 100, 16763904);
		}
	}
}
