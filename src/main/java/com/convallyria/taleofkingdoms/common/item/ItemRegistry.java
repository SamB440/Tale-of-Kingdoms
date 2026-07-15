package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import com.convallyria.taleofkingdoms.common.item.common.ItemPouch;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    public static final Map<TOKItem, Item> ITEMS = new HashMap<>();
    public static final ResourceKey<CreativeModeTab> TOK_ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "general"));

    static {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TOK_ITEM_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ITEMS.get(TOKItem.COIN)))
                .title(Component.translatable("taleofkingdoms.group.general"))
                .displayItems((context, entries) -> {
                    entries.accept(ITEMS.get(TOKItem.COIN));
                    entries.accept(ITEMS.get(TOKItem.POUCH));
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
        ITEMS.put(TOKItem.COIN, new ItemCoin(new Item.Properties()
                .stacksTo(16)
                .rarity(Rarity.COMMON)
                .fireResistant()));
        ITEMS.put(TOKItem.POUCH, new ItemPouch(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)));
        registerItems();
    }

    public static void registerItems() {
        TaleOfKingdoms.LOGGER.info("Loading items...");
        int index = 1;
        for (TOKItem item : ITEMS.keySet()) {
            TaleOfKingdoms.LOGGER.info("[{}/{}] Loading item: {}", index, ITEMS.values().size(), item.getRegistryName());
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, item.getRegistryName()), ITEMS.get(item));
            index++;
        }
    }
}
