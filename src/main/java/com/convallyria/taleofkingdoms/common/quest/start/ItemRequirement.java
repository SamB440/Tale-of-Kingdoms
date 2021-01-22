package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class ItemRequirement extends QuestRequirement {

	private ItemStack item;

	public ItemRequirement(TaleOfKingdomsAPI plugin) {
		super(plugin);
		this.item = new ItemStack(Items.IRON_AXE);
	}

	@Override
	public boolean meetsRequirements(PlayerEntity player) {
		return player.inventory.contains(item);
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
