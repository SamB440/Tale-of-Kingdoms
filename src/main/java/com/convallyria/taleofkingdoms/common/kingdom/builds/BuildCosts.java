package com.convallyria.taleofkingdoms.common.kingdom.builds;

import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;

//todo: translatable
public enum BuildCosts {
    SMALL_HOUSE_1(Text.literal("Small House"), Schematic.SMALL_HOUSE, BlockRotation.CLOCKWISE_180, KingdomPOI.TIER_ONE_SMALL_HOUSE_1, 192, 128),
    SMALL_HOUSE_2(Text.literal("Small House"), Schematic.SMALL_HOUSE, KingdomPOI.TIER_ONE_SMALL_HOUSE_2, 192, 128),
    LARGE_HOUSE(Text.literal("Large Houses"), 192, 320),
    ITEM_SHOP(Text.literal("Item Shop"), Schematic.TIER_1_BLACKSMITH_HOUSE, KingdomPOI.TIER_ONE_HOUSE_BLACKSMITH, 256, 256),
    STOCK_MARKET(Text.literal("Stock Market"), 192, 192),
    BUILDER_HOUSE(Text.literal("Builder House"), 128, 128),
    BLOCK_SHOP(Text.literal("Block Shop"), 256, 320),
    FOOD_SHOP(Text.literal("Food Shop"), 192, 256),
    BARRACKS(Text.literal("Barracks"), 320, 320),
    TAVERN(Text.literal("Tavern"), 320, 128),
    CHAPEL(Text.literal("Chapel"), 320, 320),
    LIBRARY(Text.literal("Library"), 256, 256),
    MAGE_HALL(Text.literal("Mage Hall"), 256, 320);

    private final Text displayName;
    private final Schematic schematic;
    private final BlockRotation schematicRotation;
    private final KingdomPOI kingdomPOI;
    private final int wood, stone;

    BuildCosts(Text displayName, int wood, int stone) {
        this.displayName = displayName;
        this.schematic = null;
        this.schematicRotation = null;
        this.kingdomPOI = null;
        this.wood = wood;
        this.stone = stone;
    }

    BuildCosts(Text displayName, Schematic schematic, KingdomPOI poi, int wood, int stone) {
        this(displayName, schematic, BlockRotation.NONE, poi, wood, stone);
    }

    BuildCosts(Text displayName, Schematic schematic, BlockRotation rotation, KingdomPOI poi, int wood, int stone) {
        this.displayName = displayName;
        this.schematic = schematic;
        this.schematicRotation = rotation;
        this.kingdomPOI = poi;
        this.wood = wood;
        this.stone = stone;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public KingdomPOI getKingdomPOI() {
        return kingdomPOI;
    }

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }
}
