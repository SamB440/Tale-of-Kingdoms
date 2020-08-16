package net.islandearth.taleofkingdoms.managers;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements IManager {

	private final Map<String, SoundEvent> events = new HashMap<>();
	
	public SoundManager(TaleOfKingdoms tok) {
		TaleOfKingdoms.LOGGER.info("Loading sounds...");
		Identifier toktheme = new Identifier(TaleOfKingdoms.MODID, "toktheme");
		addSound(toktheme);
		register();
	}
	
	private void addSound(Identifier location) {
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

	private void register() {
		events.forEach((name, event) -> Registry.register(Registry.SOUND_EVENT, event.getId(), event));
	}
}
