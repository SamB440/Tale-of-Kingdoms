package net.islandearth.taleofkingdoms.client.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.GUIStartConquest;
import net.minecraft.client.Minecraft;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class StartWorldListener extends Listener {
	
	private List<UUID> joined = new ArrayList<>();
	
	private boolean load(String worldName, World world) {
		TaleOfKingdoms.getAPI().get().getMod().logger.info("loading");
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		TaleOfKingdoms.getAPI().get().getMod().logger.info("part1");
		// Check if this world has been loaded or not
		if (!file.exists()) {
			TaleOfKingdoms.getAPI().get().getMod().logger.info("noexist");
			try {
				// If not, create new file
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {
			// World already exists. Welcome back, my lord!
			//TODO add some colour
			// do stuff
			return true;
		}
	}
	
	@SubscribeEvent
	public void onLeave(PlayerLoggedOutEvent e) {
		if (joined.contains(e.player.getUniqueID())) joined.remove(e.player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onLoad(EntityJoinWorldEvent e) {
		if (Minecraft.getMinecraft().player == null) return;
		if (!e.getEntity().getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()) || e.getWorld().provider.getDimensionType() != DimensionType.OVERWORLD) return;
		if (joined.contains(e.getEntity().getUniqueID())) return;
		joined.add(e.getEntity().getUniqueID());
		TaleOfKingdoms.getAPI().get().getMod().logger.info("loading");
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		boolean loaded = load(worldName, e.getWorld());
		if (loaded) return;
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Minecraft.getMinecraft().displayGuiScreen(new GUIStartConquest(worldName, file));
			}
		}, 2000);
	}
}
