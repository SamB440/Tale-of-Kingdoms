package net.islandearth.taleofkingdoms.common.listener;

import java.util.Random;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CoinListener {
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		if (e.getSource().getTrueSource() instanceof EntityPlayer) {
			ItemHelper.dropCoins(e.getEntityLiving());
		}
	}
    
	@SubscribeEvent
	public void onPickUp(PlayerEvent.ItemPickupEvent e) {
		Random random = new Random();
		ItemStack item = e.getStack();
		if (item.getItem().equals(ItemRegistry.items.get("coin"))) {
			TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getMinecraft().getIntegratedServer().getFolderName()).get().addCoins(random.nextInt(50));;
			e.getStack().setCount(0);
			//TODO find new method to consume inventory item
		}
	}
}
