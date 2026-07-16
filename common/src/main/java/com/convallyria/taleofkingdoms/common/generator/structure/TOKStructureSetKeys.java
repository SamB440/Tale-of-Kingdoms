package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class TOKStructureSetKeys {
    public static final ResourceKey<StructureSet> BANDIT_CAMPS = of("bandit_camps");
    public static final ResourceKey<StructureSet> REFICULE_VILLAGES = of("reficule_villages");
    public static final ResourceKey<StructureSet> GATEWAYS = of("gateways");

    private static ResourceKey<StructureSet> of(String id) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id));
    }
}
