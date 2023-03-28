package com.convallyria.taleofkingdoms.common.kingdom.builds;

import com.convallyria.taleofkingdoms.common.kingdom.KingdomTier;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;

//todo: translatable
public enum BuildCosts {
    SMALL_HOUSE_1(Text.literal("Small House"), KingdomTier.TIER_ONE, Schematic.SMALL_HOUSE, BlockRotation.CLOCKWISE_180, KingdomPOI.TIER_ONE_SMALL_HOUSE_1, 192, 128),
    SMALL_HOUSE_2(Text.literal("Small House"), KingdomTier.TIER_ONE, Schematic.SMALL_HOUSE, KingdomPOI.TIER_ONE_SMALL_HOUSE_2, 192, 128),
    LARGE_HOUSE(Text.literal("Large Houses"), KingdomTier.TIER_ONE, Schematic.LARGE_HOUSE, KingdomPOI.TIER_ONE_LARGE_HOUSE, 192, 320),
    ITEM_SHOP(Text.literal("Item Shop"), KingdomTier.TIER_ONE, Schematic.TIER_1_BLACKSMITH_HOUSE, KingdomPOI.TIER_ONE_HOUSE_BLACKSMITH, 256, 256),
    STOCK_MARKET(Text.literal("Stock Market"), KingdomTier.TIER_ONE, Schematic.TIER_1_STOCK_MARKET, KingdomPOI.TIER_ONE_STOCK_MARKET, 192, 192),
    BUILDER_HOUSE(Text.literal("Builder House"), KingdomTier.TIER_TWO, 128, 128),
    BLOCK_SHOP(Text.literal("Block Shop"), KingdomTier.TIER_TWO, 256, 320),
    FOOD_SHOP(Text.literal("Food Shop"), KingdomTier.TIER_TWO, 192, 256),
    BARRACKS(Text.literal("Barracks"), KingdomTier.TIER_TWO, 320, 320),
    TAVERN(Text.literal("Tavern"), KingdomTier.TIER_TWO, 320, 128),
    CHAPEL(Text.literal("Chapel"), KingdomTier.TIER_TWO, 320, 320),
    LIBRARY(Text.literal("Library"), KingdomTier.TIER_TWO, 256, 256),
    MAGE_HALL(Text.literal("Mage Hall"), KingdomTier.TIER_TWO, 256, 320);

    private final Text displayName;
    private final KingdomTier tier;
    private final Schematic schematic;
    private final BlockRotation schematicRotation;
    private final KingdomPOI kingdomPOI;
    private final int wood, stone;

    BuildCosts(Text displayName, KingdomTier tier, int wood, int stone) {
        this(displayName, tier, null, BlockRotation.NONE, null, wood, stone);
    }

    BuildCosts(Text displayName, KingdomTier tier, Schematic schematic, KingdomPOI poi, int wood, int stone) {
        this(displayName, tier, schematic, BlockRotation.NONE, poi, wood, stone);
    }

    BuildCosts(Text displayName, KingdomTier tier, Schematic schematic, BlockRotation rotation, KingdomPOI poi, int wood, int stone) {
        this.displayName = displayName;
        this.tier = tier;
        this.schematic = schematic;
        this.schematicRotation = rotation;
        this.kingdomPOI = poi;
        this.wood = wood;
        this.stone = stone;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public KingdomTier getTier() {
        return tier;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public BlockRotation getSchematicRotation() {
        return schematicRotation;
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
