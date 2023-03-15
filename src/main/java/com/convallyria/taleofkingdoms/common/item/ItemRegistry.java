package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import com.convallyria.taleofkingdoms.common.item.common.ItemPouch;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    public static final Map<TOKItem, Item> ITEMS = new HashMap<>();
    public static final ItemGroup TOK_ITEM_GROUP = FabricItemGroup
            .builder(new Identifier(TaleOfKingdoms.MODID, "general"))
            .icon(() -> new ItemStack(ITEMS.get(TOKItem.COIN)))
            .entries((context, entries) -> {
                entries.add(ITEMS.get(TOKItem.COIN));
                entries.add(ITEMS.get(TOKItem.POUCH));
            })
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
        ITEMS.put(TOKItem.COIN, new ItemCoin(new Item.Settings()
                .maxCount(16)
                .rarity(Rarity.COMMON)
                .fireproof()));
        ITEMS.put(TOKItem.POUCH, new ItemPouch(new Item.Settings()
                .maxCount(1)
                .rarity(Rarity.COMMON)));
        registerItems();
    }

    public static void registerItems() {
        TaleOfKingdoms.LOGGER.info("Loading items...");
        int index = 1;
        for (TOKItem item : ITEMS.keySet()) {
            TaleOfKingdoms.LOGGER.info("[" + index + "/" + ITEMS.values().size() + "] Loading item: " + item.getRegistryName());
            Registry.register(Registries.ITEM, new Identifier(TaleOfKingdoms.MODID, item.getRegistryName()), ITEMS.get(item));
            index++;
        }
    }
}
