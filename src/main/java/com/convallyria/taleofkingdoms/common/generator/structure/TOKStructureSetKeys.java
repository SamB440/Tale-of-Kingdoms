package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;

public class TOKStructureSetKeys {
    public static final RegistryKey<StructureSet> BANDIT_CAMPS = of("bandit_camps");
    public static final RegistryKey<StructureSet> REFICULE_VILLAGES = of("reficule_villages");
    public static final RegistryKey<StructureSet> GATEWAYS = of("gateways");

    private static RegistryKey<StructureSet> of(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(TaleOfKingdoms.MODID, id));
    }
}
