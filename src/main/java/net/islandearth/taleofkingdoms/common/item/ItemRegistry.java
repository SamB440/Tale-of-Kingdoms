package net.islandearth.taleofkingdoms.common.item;

import java.util.HashMap;
import java.util.Map;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TaleOfKingdoms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry extends Listener {

	public static Map<String, Item> items = new HashMap<>();
	
	public static class CreativeTab extends ItemGroup {

		public CreativeTab() {
			super("taleOfKingdoms");
			this.setBackgroundImageName("item_search.png");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(items.get("coin"));
		}
		
		@Override
		public boolean hasSearchBar() {
			return true;
		}
	}
	
	public static void init() {
		items.put("coin", new ItemCoin(new Item.Properties().group(new CreativeTab())).setRegistryName(TaleOfKingdoms.MODID, "coin"));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		TaleOfKingdoms.LOGGER.info("load items");
		items.values().forEach(item -> event.getRegistry().register(item));
	}
}
