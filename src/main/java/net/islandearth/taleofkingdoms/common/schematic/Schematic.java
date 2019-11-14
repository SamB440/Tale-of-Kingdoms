package net.islandearth.taleofkingdoms.common.schematic;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
	GUILD_CASTLE("/assets/schematics/GuildCastle.schem"),
	GUILD_CASTLE_OLD("/assets/schematics/GuildCastle.schematic");
	
	private final String path;
	
	Schematic(String path) {
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
