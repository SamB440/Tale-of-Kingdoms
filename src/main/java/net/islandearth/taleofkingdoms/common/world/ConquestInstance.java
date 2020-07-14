package net.islandearth.taleofkingdoms.common.world;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class ConquestInstance {
	
	private String world;
	private String name;
	private int coins;
	private boolean hasLoaded;
	private long farmerLastBread;
	private boolean hasContract;
	private int worthiness;
	private BlockPos start;
	private BlockPos end;
	
	public ConquestInstance(String world, String name, BlockPos start, BlockPos end) {
		Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
			.map(TaleOfKingdomsAPI::getConquestInstanceStorage)
			.orElseThrow(() -> new IllegalArgumentException("API not present"))
		.getConquestInstance(world);
		if (instance.isPresent() && instance.get().isLoaded()) throw new IllegalArgumentException("World already registered");
		this.world = world;
		this.name = name;
		this.start = start;
		this.end = end;
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

	public void setCoins(int coins) {
		this.coins = coins;
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

	public int getWorthiness() {
		return worthiness;
	}

	public void setWorthiness(int worthiness) {
		this.worthiness = worthiness;
	}

	public void addWorthiness(int worthiness) {
		this.worthiness = this.worthiness + worthiness;
	}

	public BlockPos getStart() {
		return start;
	}

	public BlockPos getEnd() {
		return end;
	}
}
