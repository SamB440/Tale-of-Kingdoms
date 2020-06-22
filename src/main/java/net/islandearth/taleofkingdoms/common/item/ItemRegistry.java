package net.islandearth.taleofkingdoms.common.item;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = TaleOfKingdoms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry extends Listener {

	public static final Map<TOKItem, Item> ITEMS = new HashMap<>();

	public enum TOKItem {
		COIN
	}

	public static class CreativeTab extends ItemGroup {

		public CreativeTab() {
			super("taleOfKingdoms");
			this.setBackgroundImageName("item_search.png");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ITEMS.get(TOKItem.COIN));
		}
		
		@Override
		public boolean hasSearchBar() {
			return true;
		}
	}
	
	public static void init() {
		ITEMS.put(TOKItem.COIN, new ItemCoin(new Item.Properties().group(new CreativeTab())).setRegistryName(TaleOfKingdoms.MODID, "coin"));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		TaleOfKingdoms.LOGGER.info("Loading items...");
		int index = 1;
		for (Item item : ITEMS.values()) {
			TaleOfKingdoms.LOGGER.info("[" + index + "/" + ITEMS.values().size() + "] Loading item: " + item.getClass().getName());
			event.getRegistry().register(item);
			index++;
		}
	}
}
