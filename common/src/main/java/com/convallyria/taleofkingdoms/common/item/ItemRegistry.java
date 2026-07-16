package com.convallyria.taleofkingdoms.common.item;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.item.common.ItemCoin;
import com.convallyria.taleofkingdoms.common.item.common.ItemPouch;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import java.util.EnumMap;
import java.util.Map;

public class ItemRegistry extends Listener {

    // Deferred registration (see EntityTypes for the NeoForge freeze rationale).
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTRY = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.CREATIVE_MODE_TAB);

    public static final Map<TOKItem, RegistrySupplier<Item>> ITEMS = new EnumMap<>(TOKItem.class);

    static {
        ITEMS.put(TOKItem.COIN, ITEM_REGISTRY.register(TOKItem.COIN.getRegistryName(), () -> new ItemCoin(new Item.Properties()
                .stacksTo(16)
                .rarity(Rarity.COMMON)
                .fireResistant())));
        ITEMS.put(TOKItem.POUCH, ITEM_REGISTRY.register(TOKItem.POUCH.getRegistryName(), () -> new ItemPouch(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON))));
    }

    public static final RegistrySupplier<CreativeModeTab> TOK_ITEM_GROUP = CREATIVE_TAB_REGISTRY.register("general", () ->
            CreativeTabRegistry.create(builder -> builder
                    .icon(() -> new ItemStack(ITEMS.get(TOKItem.COIN).get()))
                    .title(Component.translatable("taleofkingdoms.group.general"))
                    .displayItems((context, entries) -> {
                        entries.accept(ITEMS.get(TOKItem.COIN).get());
                        entries.accept(ITEMS.get(TOKItem.POUCH).get());
                    })));

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

    /**
     * Commits the deferred item and creative-tab registrations. Invoked from the common initializer
     * during mod construction, before the loader freezes its registries.
     */
    public static void init() {
        TaleOfKingdoms.LOGGER.info("Loading items...");
        ITEM_REGISTRY.register();
        CREATIVE_TAB_REGISTRY.register();
    }
}
