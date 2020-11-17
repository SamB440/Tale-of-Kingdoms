package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
    GUILD_CASTLE("/assets/schematics/GuildCastleNew.schem"),
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

    /**
     * Saves all schematics specified in this enum.
     * @throws IOException if <code>source</code> URL cannot be opened
     * @throws IOException if <code>destination</code> is a directory
     * @throws IOException if <code>destination</code> cannot be written
     * @throws IOException if <code>destination</code> needs creating but can't be
     * @throws IOException if an IO error occurs during copying
     */
    public static void saveAll() throws IOException {
        for (Schematic schematic : values()) {
            URL inputUrl = TaleOfKingdoms.class.getResource(schematic.getPath());
            File dest = new File(TaleOfKingdoms.getAPI().get().getDataFolder() + schematic.getPath());
            FileUtils.copyURLToFile(inputUrl, dest);
        }
    }
}
