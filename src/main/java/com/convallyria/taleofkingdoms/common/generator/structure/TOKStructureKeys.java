package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public class TOKStructureKeys {
    public static final RegistryKey<Structure> REFICULE_VILLAGE = of("reficule_village");
    public static final RegistryKey<Structure> GATEWAY = of("gateway");

    private static RegistryKey<Structure> of(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(TaleOfKingdoms.MODID, id));
    }
}
