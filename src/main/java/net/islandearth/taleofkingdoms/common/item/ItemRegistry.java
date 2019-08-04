package net.islandearth.taleofkingdoms.common.item;

import java.util.HashMap;
import java.util.Map;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TaleOfKingdoms.MODID)
public class ItemRegistry extends Listener {

	public static Map<String, Item> items = new HashMap<>();
	
	public static final CreativeTabs tab = (new CreativeTabs("creativeTab") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(items.get("coin"));
		}
		
		@Override
		public boolean hasSearchBar() {
			return true;
		}
		
	}.setBackgroundImageName("item_search.png"));
	
	public static void init() {
		items.put("coin", new ItemCoin("coin").setCreativeTab(tab));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		System.out.print("load items");
		items.values().forEach(item -> event.getRegistry().register(item));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		System.out.print("Load models");
		items.values().forEach(item -> registerRender(item));
	}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
