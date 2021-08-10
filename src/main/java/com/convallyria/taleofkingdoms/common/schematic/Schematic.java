package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.resources.ResourceLocation;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
    GUILD_CASTLE(new ResourceLocation(TaleOfKingdoms.MODID, "guild/guild"));
    //GUILD_CASTLE_OLD(new Identifier(TaleOfKingdoms.MODID, "/assets/schematics/GuildCastle.schematic"));

    private final ResourceLocation path;

    Schematic(ResourceLocation path) {
        this.path = path;
    }

    public ResourceLocation getPath() {
        return path;
    }
}
