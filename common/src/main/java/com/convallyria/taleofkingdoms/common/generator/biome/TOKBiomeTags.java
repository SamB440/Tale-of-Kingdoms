package com.convallyria.taleofkingdoms.common.generator.biome;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class TOKBiomeTags {

    public static final TagKey<Biome> SOLID_SURFACE = of("has_structure/solid_surface");
    public static final TagKey<Biome> NO_MOUNTAINS_DESERTS = of("has_structure/no_mountains_deserts");

    private TOKBiomeTags() {}

    private static TagKey<Biome> of(String id) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id));
    }
}