package net.islandearth.taleofkingdoms.client.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StartWorldListener extends Listener {
	
	@SubscribeEvent
	public void onEnter(EntityJoinWorldEvent ejwe) {
		Entity entity = ejwe.getEntity();
		File file = new File("/" + TaleOfKingdoms.MODID + "/");
		// Check if this world has been loaded or not
		if (!file.exists()) {
			try {
				// If not, create new file and save json information to it
				file.createNewFile();
				Gson gson = new Gson();
				ConquestInstance instance = new ConquestInstance(entity.world);
				gson.toJson(instance, new FileWriter(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// World already exists. Welcome back, my lord!
			//TODO add some colour
			entity.sendMessage(new TextComponentString("Welcome back, my lord. Godspeed!"));
		}
	}
}
