package net.islandearth.taleofkingdoms.common.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.item.common.ItemCoin;
import net.islandearth.taleofkingdoms.common.item.common.ItemPouch;
import net.islandearth.taleofkingdoms.common.listener.Listener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    public static final Map<TOKItem, Item> ITEMS = new HashMap<>();
    public static final ItemGroup TOK_ITEM_GROUP = FabricItemGroupBuilder.create(
            new Identifier(TaleOfKingdoms.MODID, "general"))
            .icon(() -> new ItemStack(ITEMS.get(TOKItem.COIN)))
            .build();

    public enum TOKItem {
        COIN("coin"),
        POUCH("pouch");

        private final String registryName;

        TOKItem(String registryName) {
            this.registryName = registryName;
        }

        public String getRegistryName() {
            return registryName;
        }
    }

    public static void init() {
        ITEMS.put(TOKItem.COIN, new ItemCoin(new Item.Settings().group(TOK_ITEM_GROUP)
                .maxCount(16)
                .rarity(Rarity.COMMON)
                .fireproof()));
        ITEMS.put(TOKItem.POUCH, new ItemPouch(new Item.Settings().group(TOK_ITEM_GROUP)
                .maxCount(1)
                .rarity(Rarity.COMMON)));
        registerItems();
    }

    public static void registerItems() {
        TaleOfKingdoms.LOGGER.info("Loading items...");
        int index = 1;
        for (TOKItem item : ITEMS.keySet()) {
            TaleOfKingdoms.LOGGER.info("[" + index + "/" + ITEMS.values().size() + "] Loading item: " + item.getRegistryName());
            Registry.register(Registry.ITEM, new Identifier(TaleOfKingdoms.MODID, item.getRegistryName()), ITEMS.get(item));
            index++;
        }
    }
}
