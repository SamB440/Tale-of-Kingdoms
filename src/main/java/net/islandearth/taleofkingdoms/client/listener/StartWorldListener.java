package net.islandearth.taleofkingdoms.client.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class StartWorldListener extends Listener {
	
	private List<UUID> hasEntered = new ArrayList<>();
	
	@SubscribeEvent
	public void onEnter(EntityJoinWorldEvent ejwe) {
		// Use this event because it supports both server AND client
		if (!ejwe.getWorld().isRemote) {
			load(ejwe.getWorld().getWorldInfo().getWorldName());
		} else load(Minecraft.getMinecraft().getIntegratedServer().getFolderName());
	}
	
	@SubscribeEvent
	public void onLeave(PlayerLoggedOutEvent ploe) {
		EntityPlayer player = ploe.player;
		if (hasEntered.contains(player.getUniqueID())) hasEntered.remove(player.getUniqueID());
	}
	
	private void load(String worldName) {
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		// Check if this world has been loaded or not
		if (!file.exists()) {
			try {
				// If not, create new file and save json information to it
				file.createNewFile();
				ConquestInstance instance = new ConquestInstance(worldName);
				try (Writer writer = new FileWriter(file)) {
				    Gson gson = new GsonBuilder().setPrettyPrinting().create();
				    gson.toJson(instance, writer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// World already exists. Welcome back, my lord!
			//TODO add some colour
			// do stuff
		}
	}
}
