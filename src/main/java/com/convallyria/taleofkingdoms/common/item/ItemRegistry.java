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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    public static final Map<TOKItem, Item> ITEMS = new HashMap<>();
    public static final RegistryKey<ItemGroup> TOK_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(TaleOfKingdoms.MODID, "general"));

    static {
        Registry.register(Registries.ITEM_GROUP, TOK_ITEM_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ITEMS.get(TOKItem.COIN)))
                .displayName(Text.translatable("taleofkingdoms.group.general"))
                .entries((context, entries) -> {
                    entries.add(ITEMS.get(TOKItem.COIN));
                    entries.add(ITEMS.get(TOKItem.POUCH));
                })
               .build()); // build() no longer registers by itself
    }

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
