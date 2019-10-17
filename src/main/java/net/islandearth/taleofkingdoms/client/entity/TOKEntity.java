package net.islandearth.taleofkingdoms.client.entity;

public interface TOKEntity {
	
	public boolean isStationary();
	
	public default boolean doesLookAtPlayer() {
		return true;
	}
	
	public default boolean canBePushed() {
		return false;
	}
}
