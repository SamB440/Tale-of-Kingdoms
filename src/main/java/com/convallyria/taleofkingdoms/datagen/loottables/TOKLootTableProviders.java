package com.convallyria.taleofkingdoms.datagen.loottables;

import com.convallyria.taleofkingdoms.common.loot.TOKLootTables;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;

import java.util.List;

public class TOKLootTableProviders {
	public static LootTableProvider createVanillaProvider(DataOutput output) {
		return new LootTableProvider(output, TOKLootTables.getAll(), List.of(new LootTableProvider.LootTypeGenerator(TOKChestLootTableGenerator::new, LootContextTypes.CHEST)));
	}
}