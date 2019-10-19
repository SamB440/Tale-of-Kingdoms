package net.islandearth.taleofkingdoms.managers;

import java.util.HashMap;
import java.util.Map;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundManager implements IManager {

	private Map<String, SoundEvent> events = new HashMap<>();
	
	public SoundManager(TaleOfKingdoms tok) {
		TaleOfKingdoms.LOGGER.info("Loading sounds...");
		TaleOfKingdoms.LOGGER.info("Loading sound: toktheme");
		ResourceLocation location = new ResourceLocation(TaleOfKingdoms.MODID, "toktheme");
		SoundEvent event = new SoundEvent(location);
		events.put("toktheme", event);
	}
	
	public SoundEvent getSound(String name) {
		return events.get(name);
	}
	
	@Override
	public String getName() {
		return "Sound Manager";
	}
	
	@SubscribeEvent
	public void register(RegistryEvent.Register<SoundEvent> evt) {
		events.forEach((name, event) -> {
			evt.getRegistry().register(event);
		});
	}
}
