package com.convallyria.taleofkingdoms.common.loot;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.google.common.collect.Sets;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Set;

public class TOKLootTables {
	private static final Set<RegistryKey<LootTable>> LOOT_TABLES = Sets.newHashSet();
	private static final Set<RegistryKey<LootTable>> LOOT_TABLES_READ_ONLY;
	public static final RegistryKey<LootTable> SMALL_BANDIT_CAMP;
	public static final RegistryKey<LootTable> REFICULE_VILLAGE_WELL;
	public static final RegistryKey<LootTable> REFICULE_VILLAGE_HOUSE;
	public static final RegistryKey<LootTable> REFICULE_VILLAGE_TOWER;

	private static RegistryKey<LootTable> register(String id) {
		return registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(TaleOfKingdoms.MODID, id)));
	}

	private static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> id) {
		if (LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id.getValue() + " is already a registered built-in loot table");
		}
	}

	public static Set<RegistryKey<LootTable>> getAll() {
		return LOOT_TABLES_READ_ONLY;
	}

	static {
		LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
		SMALL_BANDIT_CAMP = register("chests/small_bandit_camp");
		REFICULE_VILLAGE_WELL = register("chests/reficule_village_well");
		REFICULE_VILLAGE_HOUSE = register("chests/reficule_village_house");
		REFICULE_VILLAGE_TOWER = register("chests/reficule_village_tower");
	}
}
