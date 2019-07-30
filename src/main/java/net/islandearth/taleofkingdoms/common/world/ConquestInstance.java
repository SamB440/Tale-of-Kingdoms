package net.islandearth.taleofkingdoms.common.world;

import net.minecraft.world.World;

public class ConquestInstance implements ConquestWorld {
	
	private World world;
	
	public ConquestInstance(World world) {
		this.world = world;
	}

	@Override
	public World getWorld() {
		return world;
	}

}
