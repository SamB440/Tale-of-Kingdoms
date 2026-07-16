package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class TOKStructureKeys {
    public static final ResourceKey<Structure> BANDIT_CAMP = of("bandit_camp");
    public static final ResourceKey<Structure> GATEWAY = of("gateway");
    public static final ResourceKey<Structure> REFICULE_VILLAGE = of("reficule_village");
    public static final ResourceKey<Structure> GUILD = of("guild");

    private static ResourceKey<Structure> of(String id) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id));
    }
}
