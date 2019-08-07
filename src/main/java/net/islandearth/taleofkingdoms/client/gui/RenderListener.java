package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderListener extends Listener {

	@SubscribeEvent
	public void onRender(GuiScreenEvent.DrawScreenEvent.Post e) {
		if (e.getGui() instanceof InventoryScreen) {
			InventoryScreen gui = (InventoryScreen) e.getGui();
			String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
			if (!TaleOfKingdoms
					.getAPI()
					.get()
					.getConquestInstanceStorage()
					.getConquestInstance(worldName).isPresent()) return;
			gui.drawString(gui.getMinecraft().fontRenderer, "Gold Coins: " + TaleOfKingdoms
					.getAPI()
					.get()
					.getConquestInstanceStorage()
					.getConquestInstance(worldName)
					.get().getCoins(), gui.width / 2 - 60, gui.height / 2 - 100, 16763904);
		}
	}
}
