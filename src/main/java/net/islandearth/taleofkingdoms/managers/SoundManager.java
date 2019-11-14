package net.islandearth.taleofkingdoms.managers;

import java.util.HashMap;
import java.util.Map;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundManager implements IManager {

	private final Map<String, SoundEvent> events = new HashMap<>();
	
	public SoundManager(TaleOfKingdoms tok) {
		TaleOfKingdoms.LOGGER.info("Loading sounds...");
		ResourceLocation toktheme = new ResourceLocation(TaleOfKingdoms.MODID, "toktheme");
		this.addSound(toktheme);
	}
	
	public void addSound(ResourceLocation location) {
		TaleOfKingdoms.LOGGER.info("Loading sound: " + location.getPath());
		events.put(location.getPath(), new SoundEvent(location));
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
		events.forEach((name, event) -> evt.getRegistry().register(event));
	}
}
