package com.convallyria.taleofkingdoms.common.generator.biome;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class TOKBiomeTags {

    public static final TagKey<Biome> SOLID_SURFACE = of("has_structure/solid_surface");
    public static final TagKey<Biome> NO_MOUNTAINS_DESERTS = of("has_structure/no_mountains_deserts");

    private TOKBiomeTags() {}

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, Identifier.of(TaleOfKingdoms.MODID, id));
    }
}