package net.islandearth.taleofkingdoms.common.world;

import java.util.Optional;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;

public class ConquestInstance {
	
	private String world;
	private String name;
	private int coins;
	
	public ConquestInstance(String world, String name, int coins) {
		Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
			.map(TaleOfKingdomsAPI::getConquestInstanceStorage)
			.orElseThrow(() -> new IllegalArgumentException("API not present"))
		.getConquestInstance(world);
		if (instance.isPresent()) throw new IllegalArgumentException("World already registered");
		this.world = world;
		this.name = name;
		this.coins = coins;
	}

	public String getWorld() {
		return world;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCoins() {
		return coins;
	}

}
