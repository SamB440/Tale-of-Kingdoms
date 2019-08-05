package net.islandearth.taleofkingdoms.common.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.GUIContinueConquest;
import net.islandearth.taleofkingdoms.client.gui.GUIStartConquest;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StartWorldListener extends Listener {
	
	private boolean joined;
	
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
	public void onLeave(WorldEvent.Unload e) {
		if (e.getWorld().getDimension().getType() != DimensionType.OVERWORLD) return;
		if (!joined) return;
		
		String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(worldName).get();
		try (Writer writer = new FileWriter(file)) {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    gson.toJson(instance, writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().removeConquest(worldName);
		this.joined = false;
	}
	
	@SubscribeEvent
	public void onLoad(EntityEvent.EntityConstructing e) {
		// Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
		// Important: We only want to effect the overworld - so check the dimension type.
		if (e.getEntity() instanceof EntityPlayer) {
			if (e.getEntity().getEntityWorld().getDimension().getType() != DimensionType.OVERWORLD) return;
			if (joined) return;
			
			this.joined = true;
			//TODO support for server
			String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
			boolean loaded = load(worldName, e.getEntity().getEntityWorld());
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
							if (instance == null || instance.getName() == null) Minecraft.getInstance().displayGuiScreen(new GUIStartConquest(worldName, file));
							else {
								Minecraft.getInstance().displayGuiScreen(new GUIContinueConquest(instance));
								TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance);
							}
							
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
					Minecraft.getInstance().displayGuiScreen(new GUIStartConquest(worldName, file));
				}
			}, 1000);
		}
	}
}
