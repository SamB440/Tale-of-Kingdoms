package com.convallyria.taleofkingdoms.common.generator.util;

import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashMap;
import java.util.Map;

public class StructureConfigCreator {

    private TagKey<Biome> biomes;
    private final Map<SpawnGroup, StructureSpawns> spawns;
    private GenerationStep.Feature step;
    private StructureTerrainAdaptation terrainAdaptation;

    private StructureConfigCreator() {
        this.biomes = TOKBiomeTags.SOLID_SURFACE;
        this.spawns = new HashMap<>();
        this.step = GenerationStep.Feature.SURFACE_STRUCTURES;
        this.terrainAdaptation = StructureTerrainAdaptation.NONE;
    }

    public static StructureConfigCreator create() {
        return new StructureConfigCreator();
    }

    public StructureConfigCreator biome(TagKey<Biome> tag) {
        this.biomes = tag;
        return this;
    }

    public StructureConfigCreator terrainAdaptation(StructureTerrainAdaptation terrainAdaptation) {
        this.terrainAdaptation = terrainAdaptation;
        return this;
    }

    public StructureConfigCreator step(GenerationStep.Feature step) {
        this.step = step;
        return this;
    }

    public Structure.Config build(Registerable<Structure> structureRegisterable) {
        RegistryEntryLookup<Biome> registryEntryLookup = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        return new Structure.Config(registryEntryLookup.getOrThrow(biomes), spawns, step, terrainAdaptation);
    }
}
