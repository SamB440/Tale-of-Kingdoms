package net.islandearth.taleofkingdoms.common.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.ScreenContinueConquest;
import net.islandearth.taleofkingdoms.client.gui.ScreenStartConquest;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;

public class StartWorldListener extends Listener {
	
	private boolean joined;
	
	private boolean load(String worldName) {
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
		if (!TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().isPresent()) return;
		
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + instance.getWorld() + ".conquestworld");
		try (Writer writer = new FileWriter(file)) {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    gson.toJson(instance, writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().removeConquest(instance.getWorld());
		this.joined = false;
	}
	
	@SubscribeEvent
	public void onLoad(EntityEvent.EntityConstructing e) {
		// Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
		// Important: We only want to effect the overworld - so check the dimension type.
		if (e.getEntity() instanceof PlayerEntity) {
			if (e.getEntity().getEntityWorld().getDimension().getType() != DimensionType.OVERWORLD) return;
			if (joined) return;
			
			this.joined = true;
			//TODO support for server
			String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
			boolean loaded = load(worldName);
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
							Minecraft.getInstance().runImmediately(() -> {
								// Check if file exists, but values don't. Game probably crashed?
								if ((instance == null || instance.getName() == null) || !instance.isLoaded()) 
									Minecraft.getInstance().displayGuiScreen(new ScreenStartConquest(worldName, file, (PlayerEntity) e.getEntity()));
								else 
									Minecraft.getInstance().displayGuiScreen(new ScreenContinueConquest(instance));
									TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
							});
							
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, 5000);
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
					Minecraft.getInstance().runImmediately(() -> Minecraft.getInstance().displayGuiScreen(new ScreenStartConquest(worldName, file, (PlayerEntity) e.getEntity())));
				}
			}, 5000);
		}
	}
}
