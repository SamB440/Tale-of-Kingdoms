package com.convallyria.taleofkingdoms.common.generator.util;

import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

public class StructureConfigCreator {

    private TagKey<Biome> biomes;
    private final Map<MobCategory, StructureSpawnOverride> spawns;
    private GenerationStep.Decoration step;
    private TerrainAdjustment terrainAdaptation;

    private StructureConfigCreator() {
        this.biomes = TOKBiomeTags.SOLID_SURFACE;
        this.spawns = new HashMap<>();
        this.step = GenerationStep.Decoration.SURFACE_STRUCTURES;
        this.terrainAdaptation = TerrainAdjustment.NONE;
    }

    public static StructureConfigCreator create() {
        return new StructureConfigCreator();
    }

    public StructureConfigCreator biome(TagKey<Biome> tag) {
        this.biomes = tag;
        return this;
    }

    public StructureConfigCreator terrainAdaptation(TerrainAdjustment terrainAdaptation) {
        this.terrainAdaptation = terrainAdaptation;
        return this;
    }

    public StructureConfigCreator step(GenerationStep.Decoration step) {
        this.step = step;
        return this;
    }

    public Structure.StructureSettings build(BootstrapContext<Structure> structureRegisterable) {
        HolderGetter<Biome> registryEntryLookup = structureRegisterable.lookup(Registries.BIOME);
        return new Structure.StructureSettings(registryEntryLookup.getOrThrow(biomes), spawns, step, terrainAdaptation);
    }
}
