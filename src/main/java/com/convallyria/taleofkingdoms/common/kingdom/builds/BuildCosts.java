package com.convallyria.taleofkingdoms.common.kingdom.builds;

import net.minecraft.text.Text;

//todo: translatable
public enum BuildCosts {
    SMALL_HOUSE(Text.literal("Small Houses"), 192, 128),
    LARGE_HOUSE(Text.literal("Large Houses"), 192, 320),
    ITEM_SHOP(Text.literal("Item Shop"), 256, 256),
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
    private final int wood, stone;

    BuildCosts(Text displayName, int wood, int stone) {
        this.displayName = displayName;
        this.wood = wood;
        this.stone = stone;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }
}