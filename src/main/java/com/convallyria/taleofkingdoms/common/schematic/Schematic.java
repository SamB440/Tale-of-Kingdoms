package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
    GUILD_CASTLE(new Identifier(TaleOfKingdoms.MODID, "guild/guild"));
    //GUILD_CASTLE_OLD(new Identifier(TaleOfKingdoms.MODID, "/assets/schematics/GuildCastle.schematic"));

    private final Identifier path;

    Schematic(Identifier path) {
        this.path = path;
    }

    public Identifier getPath() {
        return path;
    }
}
