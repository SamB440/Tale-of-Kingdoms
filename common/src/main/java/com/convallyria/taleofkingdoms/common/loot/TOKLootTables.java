package com.convallyria.taleofkingdoms.common.loot;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class TOKLootTables {
	private static final Set<ResourceKey<LootTable>> LOOT_TABLES = Sets.newHashSet();
	private static final Set<ResourceKey<LootTable>> LOOT_TABLES_READ_ONLY;
	public static final ResourceKey<LootTable> SMALL_BANDIT_CAMP;
	public static final ResourceKey<LootTable> REFICULE_VILLAGE_WELL;
	public static final ResourceKey<LootTable> REFICULE_VILLAGE_HOUSE;
	public static final ResourceKey<LootTable> REFICULE_VILLAGE_TOWER;

	private static ResourceKey<LootTable> register(String id) {
		return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id)));
	}

	private static ResourceKey<LootTable> registerLootTable(ResourceKey<LootTable> id) {
		if (LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id.location() + " is already a registered built-in loot table");
		}
	}

	public static Set<ResourceKey<LootTable>> getAll() {
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
