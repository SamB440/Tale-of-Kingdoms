package com.convallyria.taleofkingdoms.datagen.loottables;

import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.loot.TOKLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TOKChestLootTableGenerator extends SimpleFabricLootTableProvider {

	public TOKChestLootTableGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup, LootContextParamSets.BLOCK);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
		exporter.accept(TOKLootTables.SMALL_BANDIT_CAMP, LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setBonusRolls(ConstantValue.exactly(0))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(15)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
						.add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(8)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
						.add(LootItem.lootTableItem(Items.MUTTON).setWeight(12)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
						.add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(2))
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE).setWeight(4))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(4))
						.add(LootItem.lootTableItem(Items.WHEAT).setWeight(10)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 12))))
						.add(LootItem.lootTableItem(ItemRegistry.ITEMS.get(ItemRegistry.TOKItem.COIN)).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 32))))
						.setRolls(UniformGenerator.between(2, 8))
		));

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_WELL, LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setBonusRolls(ConstantValue.exactly(0))
						.add(LootItem.lootTableItem(Items.ARROW).setWeight(14)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
						.add(LootItem.lootTableItem(Items.COD).setWeight(12)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
						.add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(9)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
						.add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 7))))
						.setRolls(UniformGenerator.between(3, 7))
		));

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_HOUSE, LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setBonusRolls(ConstantValue.exactly(0))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(20)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 12))))
						.add(LootItem.lootTableItem(Items.BREAD).setWeight(12)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
						.add(LootItem.lootTableItem(Items.HONEY_BOTTLE).setWeight(10)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(Items.HONEYCOMB).setWeight(8)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
						.add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
						.setRolls(UniformGenerator.between(3, 7))
		));

		exporter.accept(TOKLootTables.REFICULE_VILLAGE_TOWER, LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setBonusRolls(ConstantValue.exactly(0))
						.add(LootItem.lootTableItem(Items.ARROW).setWeight(14)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 16))))
						.add(LootItem.lootTableItem(Items.TORCH).setWeight(12)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 12))))
						.add(LootItem.lootTableItem(Items.BREAD).setWeight(10)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
						.add(LootItem.lootTableItem(Items.APPLE).setWeight(10)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))))
						.add(LootItem.lootTableItem(Items.IRON_SHOVEL).setWeight(8)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
						.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
						.setRolls(UniformGenerator.between(3, 7))
		));
	}
}
