package com.convallyria.taleofkingdoms.common.generator.util;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashMap;
import java.util.Map;

public class StructureConfigCreator {

    private RegistryEntryList<Biome> biomes;
    private final Map<SpawnGroup, StructureSpawns> spawns;
    private GenerationStep.Feature step;
    private StructureTerrainAdaptation terrainAdaptation;

    private StructureConfigCreator() {
        this.biomes = RegistryEntryList.of();
        this.spawns = new HashMap<>();
        this.step = GenerationStep.Feature.SURFACE_STRUCTURES;
        this.terrainAdaptation = StructureTerrainAdaptation.NONE;
    }

    public static StructureConfigCreator create() {
        return new StructureConfigCreator();
    }

    public StructureConfigCreator biome(TagKey<Biome> tag) {
        this.biomes = getOrCreateBiomeTag(tag);
        return this;
    }

    private static RegistryEntryList<Biome> getOrCreateBiomeTag(TagKey<Biome> key) {
        return BuiltinRegistries.BIOME.getOrCreateEntryList(key);
    }

    public StructureConfigCreator terrainAdaptation(StructureTerrainAdaptation terrainAdaptation) {
        this.terrainAdaptation = terrainAdaptation;
        return this;
    }

    public StructureConfigCreator step(GenerationStep.Feature step) {
        this.step = step;
        return this;
    }

    public Structure.Config build() {
        return new Structure.Config(biomes, spawns, step, terrainAdaptation);
    }
}
