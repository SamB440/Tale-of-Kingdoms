package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.regions.Region;

import java.util.UUID;

public class OperationInstance {

	private final UUID operationId;
	private final Region region;
	private final int blocks;
	
	public OperationInstance(UUID operationId, Region region) {
		this.operationId = operationId;
		this.region = region;
		this.blocks = region.getArea();
	}
	
	public UUID getOperationId() {
		return this.operationId;
	}

	public Region getRegion() {
		return region;
	}

	public int getBlocks() {
		return blocks;
	}
}
