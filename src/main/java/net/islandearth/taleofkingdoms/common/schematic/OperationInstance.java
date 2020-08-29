package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.regions.Region;

import java.util.UUID;

public class OperationInstance {

	private final UUID operationId;
	private final Region region;
	
	public OperationInstance(UUID operationId, Region region) {
		this.operationId = operationId;
		this.region = region;
	}
	
	public UUID getOperationId() {
		return this.operationId;
	}

	public Region getRegion() {
		return region;
	}
}
