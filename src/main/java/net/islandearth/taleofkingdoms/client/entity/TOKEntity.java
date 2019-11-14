package net.islandearth.taleofkingdoms.client.entity;

public interface TOKEntity {
	
	boolean isStationary();
	
	default boolean doesLookAtPlayer() {
		return true;
	}
	
	default boolean canBePushed() {
		return false;
	}
}
