package net.islandearth.taleofkingdoms.common.config;

import net.minecraftforge.fml.config.ModConfig;

public class Configuration {

	private static ModConfig clientConfig;
	private static ModConfig serverConfig;
	
	public static void bakeClient(final ModConfig config) {
		clientConfig = config;

	}

	private static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
	}
}
