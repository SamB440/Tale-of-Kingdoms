package net.islandearth.taleofkingdoms.common.world;

import java.util.Optional;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;

public class ConquestInstance {
	
	private String world;
	
	public ConquestInstance(String world) {
		Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
			.map(TaleOfKingdomsAPI::getConquestInstanceStorage)
			.orElseThrow(() -> new IllegalArgumentException("API not present"))
		.getConquestInstance(world);
		if (instance.isPresent()) throw new IllegalArgumentException("World already registered");
		this.world = world;
	}

	public String getWorld() {
		return world;
	}

}
