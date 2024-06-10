package com.convallyria.taleofkingdoms.common.kingdom.builds;

import com.convallyria.taleofkingdoms.common.kingdom.KingdomTier;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.serialization.EnumCodec;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;

public enum BuildCosts implements EnumCodec.Values {
    SMALL_HOUSE_1(Text.translatable("menu.taleofkingdoms.citybuilder.small_house"), KingdomTier.TIER_ONE, Schematic.TIER_1_SMALL_HOUSE_VARIANT_ONE, BlockRotation.COUNTERCLOCKWISE_90, KingdomPOI.TIER_ONE_SMALL_HOUSE_1, 192, 128),
    SMALL_HOUSE_2(Text.translatable("menu.taleofkingdoms.citybuilder.small_house"), KingdomTier.TIER_ONE, Schematic.TIER_1_SMALL_HOUSE_VARIANT_TWO, BlockRotation.CLOCKWISE_180, KingdomPOI.TIER_ONE_SMALL_HOUSE_2, 192, 128),
    LARGE_HOUSE(Text.translatable("menu.taleofkingdoms.citybuilder.large_house"), KingdomTier.TIER_ONE, Schematic.TIER_1_LARGE_HOUSE, BlockRotation.COUNTERCLOCKWISE_90, KingdomPOI.TIER_ONE_LARGE_HOUSE, 192, 320),
    ITEM_SHOP(Text.translatable("menu.taleofkingdoms.citybuilder.item_shop"), KingdomTier.TIER_ONE, Schematic.TIER_1_BLACKSMITH_HOUSE, KingdomPOI.TIER_ONE_HOUSE_BLACKSMITH, 256, 256),
    STOCK_MARKET(Text.translatable("menu.taleofkingdoms.citybuilder.stock_market"), KingdomTier.TIER_ONE, Schematic.TIER_1_STOCK_MARKET, KingdomPOI.TIER_ONE_STOCK_MARKET, 192, 192),
    TIER_2_SMALL_HOUSE_1(Text.translatable("menu.taleofkingdoms.citybuilder.small_house"), KingdomTier.TIER_TWO, Schematic.TIER_2_SMALL_HOUSE_VARIANT_ONE, KingdomPOI.TIER_TWO_SMALL_HOUSE_VARIANT_ONE, 192, 128),
    TIER_2_SMALL_HOUSE_2(Text.translatable("menu.taleofkingdoms.citybuilder.small_house"), KingdomTier.TIER_TWO, Schematic.TIER_2_SMALL_HOUSE_VARIANT_TWO, KingdomPOI.TIER_TWO_SMALL_HOUSE_VARIANT_TWO, 192, 128),
    TIER_2_LARGE_HOUSE(Text.translatable("menu.taleofkingdoms.citybuilder.large_house"), KingdomTier.TIER_TWO, Schematic.TIER_2_LARGE_HOUSE, KingdomPOI.TIER_TWO_LARGE_HOUSE, 192, 320),
    BUILDER_HOUSE(Text.translatable("menu.taleofkingdoms.citybuilder.builder_house"), KingdomTier.TIER_TWO, Schematic.TIER_2_BUILDER_HOUSE, KingdomPOI.TIER_TWO_BUILDER_HOUSE, 128, 128),
    BLOCK_SHOP(Text.translatable("menu.taleofkingdoms.citybuilder.block_shop"), KingdomTier.TIER_TWO, Schematic.TIER_2_BLOCK_SHOP, KingdomPOI.TIER_TWO_BLOCK_SHOP, 256, 320),
    FOOD_SHOP(Text.translatable("menu.taleofkingdoms.citybuilder.food_shop"), KingdomTier.TIER_TWO, Schematic.TIER_2_BAKE_HOUSE, KingdomPOI.TIER_TWO_BAKE_HOUSE, 192, 256),
    BARRACKS(Text.translatable("menu.taleofkingdoms.citybuilder.barracks"), KingdomTier.TIER_TWO, Schematic.TIER_2_BARRACKS, KingdomPOI.TIER_TWO_BARRACKS, 320, 320),
    /*TAVERN(Text.literal("Tavern"), KingdomTier.TIER_TWO, 320, 128),
    CHAPEL(Text.literal("Chapel"), KingdomTier.TIER_TWO, 320, 320),
    LIBRARY(Text.literal("Library"), KingdomTier.TIER_TWO, 256, 256),
    MAGE_HALL(Text.literal("Mage Hall"), KingdomTier.TIER_TWO, 256, 320)*/;

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

    @Override
    public String getSerializedName() {
        return name();
    }
}
