package net.islandearth.taleofkingdoms.common.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConquestInstanceStorage {

	private Map<String, ConquestInstance> conquests = new HashMap<>();
	
	public Optional<ConquestInstance> getConquestInstance(String worldName) {
		return Optional.ofNullable(conquests.get(worldName));
	}
	
	public void addConquest(String worldName, ConquestInstance instance) {
		this.conquests.put(worldName, instance);
	}
}
