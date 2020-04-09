package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.listener.Listener;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		if (e.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemHelper.dropCoins(e.getEntityLiving());
		}
	}
    
	@SubscribeEvent
	public void onPickUp(PlayerEvent.ItemPickupEvent e) {
		Random random = ThreadLocalRandom.current();
		ItemStack item = e.getStack();
		if (item.getItem().equals(ItemRegistry.items.get("coin"))) {
			TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get().addCoins(random.nextInt(50));
			e.getPlayer().inventory.clearMatchingItems(predicate -> predicate.getItem().equals(item.getItem()), -1);
		}
	}
}
