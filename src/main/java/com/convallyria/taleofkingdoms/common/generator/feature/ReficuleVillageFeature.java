package com.convallyria.taleofkingdoms.common.generator.feature;

import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ReficuleVillageFeature extends StructureFeature<DefaultFeatureConfig> {

    public ReficuleVillageFeature() {
        super(DefaultFeatureConfig.CODEC, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), ReficuleVillageFeature::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final HeightLimitView world = context.world();
        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation rotation = BlockRotation.random(context.random());
        ReficuleVillageGenerator.addPieces(context.structureManager(), pos, rotation, collector, context.random());
    }

//    @Override
//    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
//        double percent = Math.random() * 100;
//        return percent < TaleOfKingdoms.config.mainConfig.reficuleVillageSpawnRate;
//    }
}