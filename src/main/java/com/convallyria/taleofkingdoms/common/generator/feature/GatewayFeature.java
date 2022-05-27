package com.convallyria.taleofkingdoms.common.generator.feature;

import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
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

public class GatewayFeature extends StructureFeature<DefaultFeatureConfig> {

    public GatewayFeature() {
        super(DefaultFeatureConfig.CODEC, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), GatewayFeature::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final HeightLimitView world = context.world();
        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation blockRotation = BlockRotation.random(context.random());
        GatewayGenerator.addPieces(context.structureManager(), pos, blockRotation, collector, context.random());
    }

//    @Override
//    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
//        double percent = Math.random() * 100;
//        IglooGenerator
//        return percent < TaleOfKingdoms.config.mainConfig.gateWaySpawnRate;
//    }
}