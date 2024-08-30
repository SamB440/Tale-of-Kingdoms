package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public class TOKStructureKeys {
    public static final RegistryKey<Structure> BANDIT_CAMP = of("bandit_camp");
    public static final RegistryKey<Structure> GATEWAY = of("gateway");
    public static final RegistryKey<Structure> REFICULE_VILLAGE = of("reficule_village");

    private static RegistryKey<Structure> of(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(TaleOfKingdoms.MODID, id));
    }
}
