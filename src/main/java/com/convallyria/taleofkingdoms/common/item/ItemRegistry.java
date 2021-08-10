package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import com.convallyria.taleofkingdoms.common.item.common.ItemPouch;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    public static final Map<TOKItem, Item> ITEMS = new HashMap<>();
    public static final CreativeModeTab TOK_ITEM_GROUP = FabricItemGroupBuilder.create(
            new ResourceLocation(TaleOfKingdoms.MODID, "general"))
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
        ITEMS.put(TOKItem.COIN, new ItemCoin(new Item.Properties().tab(TOK_ITEM_GROUP)
                .stacksTo(16)
                .rarity(Rarity.COMMON)
                .fireResistant()));
        ITEMS.put(TOKItem.POUCH, new ItemPouch(new Item.Properties().tab(TOK_ITEM_GROUP)
                .stacksTo(1)
                .rarity(Rarity.COMMON)));
        registerItems();
    }

    public static void registerItems() {
        TaleOfKingdoms.LOGGER.info("Loading items...");
        int index = 1;
        for (TOKItem item : ITEMS.keySet()) {
            TaleOfKingdoms.LOGGER.info("[" + index + "/" + ITEMS.values().size() + "] Loading item: " + item.getRegistryName());
            Registry.register(Registry.ITEM, new ResourceLocation(TaleOfKingdoms.MODID, item.getRegistryName()), ITEMS.get(item));
            index++;
        }
    }
}
