package com.convallyria.taleofkingdoms.common.generator.feature;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReficuleVillageFeature extends StructureFeature<DefaultFeatureConfig> {

    public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
    public static final StructureFeature<DefaultFeatureConfig> REFICULE_VILLAGE_STRUCTURE = new ReficuleVillageFeature(DefaultFeatureConfig.CODEC);
    private static final ConfiguredStructureFeature<?, ?> REFICULE_VILLAGE_CONFIGURED = REFICULE_VILLAGE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);

    public static void register(int seed) {
        // Start - reficule village
        Registry.register(Registry.STRUCTURE_PIECE, new Identifier(TaleOfKingdoms.MODID, "reficule_village_piece"), REFICULE_VILLAGE);
        FabricStructureBuilder.create(new Identifier(TaleOfKingdoms.MODID, "reficule_village"), REFICULE_VILLAGE_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(48, 8, seed)
                .adjustsSurface()
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> reficuleVillage = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                new Identifier(TaleOfKingdoms.MODID, "reficule_village"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, reficuleVillage.getValue(), REFICULE_VILLAGE_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.PLAINS, Biome.Category.FOREST,
                Biome.Category.JUNGLE, Biome.Category.ICY, Biome.Category.TAIGA, Biome.Category.SAVANNA, Biome.Category.MESA), reficuleVillage);
        // End - reficule village
    }
    public ReficuleVillageFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
        double percent = Math.random() * 100;
        return percent < TaleOfKingdoms.config.mainConfig.reficuleVillageSpawnRate;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {

        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int i, long l) {
            super(structureFeature, chunkPos, i, l);
        }

        // Called when the world attempts to spawn in a new structure, and is the gap between your feature and generator.
        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator,
                         StructureManager structureManager, ChunkPos chunkPos, Biome biome,
                         DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, heightLimitView);
            BlockPos pos = new BlockPos(x, y, z);
            BlockRotation rotation = BlockRotation.random(this.random);
            ReficuleVillageGenerator.addPieces(structureManager, pos, rotation, this.children);
            this.setBoundingBoxFromChildren();
        }
    }
}