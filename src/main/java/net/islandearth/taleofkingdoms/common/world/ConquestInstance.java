package net.islandearth.taleofkingdoms.common.world;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;

import java.util.Optional;

public class ConquestInstance {
	
	private String world;
	private String name;
	private int coins;
	private boolean hasLoaded;
	private long farmerLastBread;
	private boolean hasContract;
	
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
	
	public void addCoins(int coins) {
		this.coins = this.coins + coins;
	}
	
	public boolean isLoaded() {
		return hasLoaded;
	}
	
	public void setLoaded(boolean loaded) {
		this.hasLoaded = loaded;
	}

	public long getFarmerLastBread() {
		return farmerLastBread;
	}
	
	public void setFarmerLastBread(long day) {
		this.farmerLastBread = day;
	}

	public boolean hasContract() {
		return hasContract;
	}

	public void setHasContract(boolean hasContract) {
		this.hasContract = hasContract;
	}
}
