package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.EntityDeathCallback;
import net.islandearth.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {

	public CoinListener() {
		EntityDeathCallback.EVENT.register((source, entity) -> {
			if (source.getSource() instanceof PlayerEntity) {
				ItemHelper.dropCoins(entity);
				//TODO worthiness stuff
			}
		});

		EntityPickupItemCallback.EVENT.register((entity, item, count) -> {
			if (entity instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) entity;
				if (item instanceof ItemEntity) {
					ItemEntity itemEntity = (ItemEntity) item;
					System.out.println(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN));
					System.out.println(itemEntity);
					System.out.println(itemEntity.getStack());
					System.out.println(itemEntity.getStack().getItem());
					if (itemEntity.getStack().getItem().equals(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN))) {
						System.out.println("equals");
						Random random = ThreadLocalRandom.current();
						TaleOfKingdoms.getAPI().get()
								.getConquestInstanceStorage()
								.mostRecentInstance()
								.get()
								.addCoins(random.nextInt(50));
						player.inventory.remove(predicate -> predicate.getItem().equals(itemEntity.getStack().getItem()), -1, player.inventory);
					}
				}
			}
		});
	}
}
