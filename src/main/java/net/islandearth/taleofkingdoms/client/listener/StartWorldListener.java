package net.islandearth.taleofkingdoms.client.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.GUIContinueConquest;
import net.islandearth.taleofkingdoms.client.gui.GUIStartConquest;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class StartWorldListener extends Listener {
	
	private List<UUID> joined = new ArrayList<>();
	
	private boolean load(String worldName, World world) {
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		// Check if this world has been loaded or not
		if (!file.exists()) {
			try {
				// If not, create new file
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {
			// It already exists.
			return true;
		}
	}
	
	@SubscribeEvent
	public void onLeave(PlayerLoggedOutEvent e) {
		if (joined.contains(e.player.getUniqueID())) joined.remove(e.player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onLoad(EntityJoinWorldEvent e) {
		// Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
		// Important: We only want to effect the overworld - so check the dimension type.
		if (Minecraft.getMinecraft().player == null) return;
		if (!e.getEntity().getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()) || e.getWorld().provider.getDimensionType() != DimensionType.OVERWORLD) return;
		if (joined.contains(e.getEntity().getUniqueID())) return;
		joined.add(e.getEntity().getUniqueID());
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		boolean loaded = load(worldName, e.getWorld());
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		if (loaded) {
			// Already exists
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try {
				// Load from json into class
				BufferedReader reader = new BufferedReader(new FileReader(file));
				ConquestInstance instance = gson.fromJson(reader, ConquestInstance.class);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (instance == null || instance.getName() == null) Minecraft.getMinecraft().displayGuiScreen(new GUIStartConquest(worldName, file));
						else Minecraft.getMinecraft().displayGuiScreen(new GUIContinueConquest(instance));
						
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, 1000);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		// New world creation
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Minecraft.getMinecraft().displayGuiScreen(new GUIStartConquest(worldName, file));
			}
		}, 1000);
	}
}
