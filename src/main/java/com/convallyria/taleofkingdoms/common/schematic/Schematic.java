package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

/**
 * An enum of schematics, with file paths, that are available to paste.
 */
public enum Schematic {
    GUILD_CASTLE(new Identifier(TaleOfKingdoms.MODID, "guild/guildnew")),
    TIER_1_KINGDOM(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/tier_one")),
    TIER_2_KINGDOM(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/tier_two")),
    TIER_1_BLACKSMITH_HOUSE(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/blacksmith/tier_one_house_blacksmith")),
    TIER_1_STOCK_MARKET("player_kingdom/stock_market/tier_one_stock_market"),
    TIER_1_LARGE_HOUSE("player_kingdom/large_house/tier_one_large_house_new"),
    TIER_1_SMALL_HOUSE(new Identifier(TaleOfKingdoms.MODID, "player_kingdom/small_house/tier_one_small_house")),
    TIER_1_SMALL_HOUSE_VARIANT_ONE("player_kingdom/small_house/tier_one_small_house_one_new"),
    TIER_1_SMALL_HOUSE_VARIANT_TWO("player_kingdom/small_house/tier_one_small_house_two_new"),

    TIER_2_SMALL_HOUSE_VARIANT_ONE("player_kingdom/small_house/tier_two_small_house_variant_one"),
    TIER_2_SMALL_HOUSE_VARIANT_TWO("player_kingdom/small_house/tier_two_small_house_variant_two"),
    TIER_2_LARGE_HOUSE("player_kingdom/large_house/tier_two_large_house"),
    TIER_2_BARRACKS("player_kingdom/barracks/tier_two_barracks"),
    TIER_2_BAKE_HOUSE("player_kingdom/bake_house/tier_two_bake_house"),
    TIER_2_BUILDER_HOUSE("player_kingdom/builder_house/tier_two_builder_house"),
    TIER_2_BLOCK_SHOP("player_kingdom/block_shop/tier_two_block_shop");
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
