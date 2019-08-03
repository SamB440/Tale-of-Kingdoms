package net.islandearth.taleofkingdoms.common.world;

import java.util.Optional;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;

public class ConquestInstance {
	
	private String world;
	private String name;
	
	public ConquestInstance(String world, String name) {
		Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
			.map(TaleOfKingdomsAPI::getConquestInstanceStorage)
			.orElseThrow(() -> new IllegalArgumentException("API not present"))
		.getConquestInstance(world);
		if (instance.isPresent()) throw new IllegalArgumentException("World already registered");
		this.world = world;
		this.name = name;
	}

	public String getWorld() {
		return world;
	}
	
	public String getName() {
		return name;
	}

}
