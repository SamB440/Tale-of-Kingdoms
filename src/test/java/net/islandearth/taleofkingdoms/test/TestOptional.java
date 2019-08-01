package net.islandearth.taleofkingdoms.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.islandearth.taleofkingdoms.common.world.ConquestInstanceStorage;

public class TestOptional {

	private ConquestInstanceStorage storage;
	
	@BeforeAll
	public void setUp() {
		this.storage = new ConquestInstanceStorage();
	}
	
	@Test
	public void testOptional() {
		Assertions.assertEquals(null, storage.getConquestInstance("test").get());
	}
}
