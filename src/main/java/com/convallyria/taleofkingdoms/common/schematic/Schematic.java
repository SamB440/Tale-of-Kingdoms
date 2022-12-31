package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
    GUILD_CASTLE(new Identifier(TaleOfKingdoms.MODID, "guild/guild")),
    TIER_1_KINGDOM(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/tier_one")),
    TIER_1_BLACKSMITH_HOUSE(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/blacksmith/tier_one_house_blacksmith")),
    TIER_1_STOCK_MARKET("player_kingdom/stock_market/tier_one_stock_market"),
    LARGE_HOUSE("player_kingdom/large_house/tier_one_large_house"),
    SMALL_HOUSE(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/small_house/tier_one_small_house"));
    //GUILD_CASTLE_OLD(new Identifier(TaleOfKingdoms.MODID, "/assets/schematics/GuildCastle.schematic"));

    private final Identifier path;

    Schematic(String path) {
        this(new Identifier(TaleOfKingdoms.MODID, path));
    }

    Schematic(Identifier path) {
        this.path = path;
    }

    public Identifier getPath() {
        return path;
    }
}
