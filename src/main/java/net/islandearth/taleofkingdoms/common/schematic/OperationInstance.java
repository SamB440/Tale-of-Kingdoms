package net.islandearth.taleofkingdoms.common.schematic;

import java.util.UUID;

public class OperationInstance {

	private final UUID operationId;
	private final int blocks;
	
	public OperationInstance(UUID operationId, int blocks) {
		this.operationId = operationId;
		this.blocks = blocks;
	}
	
	public UUID getOperationId() {
		return this.operationId;
	}
	
	public int getBlocks() {
		return blocks;
	}
}
