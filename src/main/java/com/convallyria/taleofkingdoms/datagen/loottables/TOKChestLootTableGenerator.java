package com.convallyria.taleofkingdoms.datagen.loottables;

import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.loot.TOKLootTables;
import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class TOKChestLootTableGenerator implements LootTableGenerator {

	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
		exporter.accept(TOKLootTables.SMALL_BANDIT_CAMP, LootTable.builder().pool(
				LootPool.builder()
						.bonusRolls(ConstantLootNumberProvider.create(0))
						.with(ItemEntry.builder(Items.APPLE).weight(15)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 6))))
						.with(ItemEntry.builder(Items.CHARCOAL).weight(8)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 6))))
						.with(ItemEntry.builder(Items.MUTTON).weight(12)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))
						.with(ItemEntry.builder(Items.IRON_SWORD).weight(2))
						.with(ItemEntry.builder(Items.STONE_PICKAXE).weight(4))
						.with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(4))
						.with(ItemEntry.builder(Items.WHEAT).weight(10)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 12))))
						.with(ItemEntry.builder(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN)).weight(2)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 32))))
						.rolls(UniformLootNumberProvider.create(2, 8))
		));
	}
}
