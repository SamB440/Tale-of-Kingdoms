package com.convallyria.taleofkingdoms.common.loot;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.google.common.collect.Sets;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Set;

public class TOKLootTables {
	private static final Set<Identifier> LOOT_TABLES = Sets.newHashSet();
	private static final Set<Identifier> LOOT_TABLES_READ_ONLY;
	public static final Identifier SMALL_BANDIT_CAMP;
	public static final Identifier REFICULE_VILLAGE_WELL;
	public static final Identifier REFICULE_VILLAGE_HOUSE;
	public static final Identifier REFICULE_VILLAGE_TOWER;

	private static Identifier register(String id) {
		return registerLootTable(new Identifier(TaleOfKingdoms.MODID, id));
	}

	private static Identifier registerLootTable(Identifier id) {
		if (LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException("" + id + " is already a registered built-in loot table");
		}
	}

	public static Set<Identifier> getAll() {
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
