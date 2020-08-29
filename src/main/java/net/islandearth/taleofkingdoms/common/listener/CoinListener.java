package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.EntityDeathCallback;
import net.islandearth.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinListener extends Listener {

	public CoinListener() {
		EntityDeathCallback.EVENT.register(((source, entity) -> {
			if (source.getSource() instanceof PlayerEntity) {
				ItemHelper.dropCoins(entity);
				//TODO worthiness stuff
			}
		}));

		EntityPickupItemCallback.EVENT.register(((entity, item, count) -> {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				if (item instanceof ItemEntity) {
					ItemEntity itemEntity = (ItemEntity) item;
					if (itemEntity.getStack().getItem().equals(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN))) {
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
		}));
	}
}
