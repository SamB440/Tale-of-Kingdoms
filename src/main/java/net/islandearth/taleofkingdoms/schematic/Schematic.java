package net.islandearth.taleofkingdoms.schematic;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;

public enum Schematic {
	GUILD_CASTLE("/assets/schematics/theguild.schem");
	
	private String path;
	
	private Schematic(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public File getFile() {
		return new File(TaleOfKingdoms.getAPI().get().getDataFolder() + this.getPath());
	}
	
    public static void saveAll() throws Exception {
    	for (Schematic schematic : values()) {
    		URL inputUrl = TaleOfKingdoms.class.getResource(schematic.getPath());
    		File dest = new File(TaleOfKingdoms.getAPI().get().getDataFolder() + schematic.getPath());
    		FileUtils.copyURLToFile(inputUrl, dest);
    	}
    }
}
