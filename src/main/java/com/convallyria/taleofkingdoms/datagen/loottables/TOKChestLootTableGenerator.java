package com.convallyria.taleofkingdoms.datagen.loottables;

import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.loot.TOKLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TOKChestLootTableGenerator extends SimpleFabricLootTableProvider {

	public TOKChestLootTableGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup, LootContextTypes.BLOCK);
	}

	@Override
	public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
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

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_WELL, LootTable.builder().pool(
				LootPool.builder()
						.bonusRolls(ConstantLootNumberProvider.create(0))
						.with(ItemEntry.builder(Items.ARROW).weight(14)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 4))))
						.with(ItemEntry.builder(Items.COD).weight(12)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 8))))
						.with(ItemEntry.builder(Items.STONE_AXE).weight(9)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))))
						.with(ItemEntry.builder(Items.CHAINMAIL_CHESTPLATE).weight(6)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))))
						.with(ItemEntry.builder(Items.IRON_INGOT).weight(3)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 7))))
						.rolls(UniformLootNumberProvider.create(3, 7))
		));

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_HOUSE, LootTable.builder().pool(
				LootPool.builder()
						.bonusRolls(ConstantLootNumberProvider.create(0))
						.with(ItemEntry.builder(Items.COAL).weight(20)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 12))))
						.with(ItemEntry.builder(Items.BREAD).weight(12)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 8))))
						.with(ItemEntry.builder(Items.HONEY_BOTTLE).weight(10)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))
						.with(ItemEntry.builder(Items.HONEYCOMB).weight(8)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
						.with(ItemEntry.builder(Items.IRON_PICKAXE).weight(6)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))))
						.with(ItemEntry.builder(Items.IRON_SWORD).weight(4)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))))
						.rolls(UniformLootNumberProvider.create(3, 7))
		));

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_TOWER, LootTable.builder().pool(
				LootPool.builder()
						.bonusRolls(ConstantLootNumberProvider.create(0))
						.with(ItemEntry.builder(Items.ARROW).weight(14)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 16))))
						.with(ItemEntry.builder(Items.TORCH).weight(12)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 12))))
						.with(ItemEntry.builder(Items.BREAD).weight(10)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 6))))
						.with(ItemEntry.builder(Items.APPLE).weight(10)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 8))))
						.with(ItemEntry.builder(Items.IRON_SHOVEL).weight(8)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1))))
						.with(ItemEntry.builder(Items.IRON_INGOT).weight(4)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 8))))
						.rolls(UniformLootNumberProvider.create(3, 7))
		));
	}
}
