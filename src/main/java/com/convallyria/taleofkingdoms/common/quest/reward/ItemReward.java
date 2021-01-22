package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class ItemReward extends QuestReward {
	
	private ItemStack item;

	public ItemReward(TaleOfKingdoms plugin) {
		super(plugin);
		this.item = new ItemStack(Items.IRON_AXE);
	}

	@Override
	public void award(PlayerEntity player) {
		player.inventory.insertStack(item);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	@Override
	public String getName() {
		return "Item";
	}
}
