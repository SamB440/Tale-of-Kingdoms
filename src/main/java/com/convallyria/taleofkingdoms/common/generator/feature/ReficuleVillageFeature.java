//package com.convallyria.taleofkingdoms.common.generator.feature;
//
//import com.convallyria.taleofkingdoms.TaleOfKingdoms;
//import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
//import com.mojang.serialization.Codec;
//import net.minecraft.structure.StructureManager;
//import net.minecraft.structure.StructureStart;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.registry.DynamicRegistryManager;
//import net.minecraft.world.HeightLimitView;
//import net.minecraft.world.Heightmap;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.source.BiomeSource;
//import net.minecraft.world.gen.ChunkRandom;
//import net.minecraft.world.gen.chunk.ChunkGenerator;
//import net.minecraft.world.gen.feature.DefaultFeatureConfig;
//import net.minecraft.world.gen.feature.StructureFeature;
//
//public class ReficuleVillageFeature extends StructureFeature<DefaultFeatureConfig> {
//
//    public ReficuleVillageFeature(Codec<DefaultFeatureConfig> codec) {
//        super(codec);
//    }
//
//    @Override
//    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
//        return Start::new;
//    }
//
//    @Override
//    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
//        double percent = Math.random() * 100;
//        return percent < TaleOfKingdoms.config.mainConfig.reficuleVillageSpawnRate;
//    }
//
//    public static class Start extends StructureStart<DefaultFeatureConfig> {
//
//        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int i, long l) {
//            super(structureFeature, chunkPos, i, l);
//        }
//
//        // Called when the world attempts to spawn in a new structure, and is the gap between your feature and generator.
//        @Override
//        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator,
//                         StructureManager structureManager, ChunkPos chunkPos, Biome biome,
//                         DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
//            int x = chunkPos.x * 16;
//            int z = chunkPos.z * 16;
//            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, heightLimitView);
//            BlockPos pos = new BlockPos(x, y, z);
//            BlockRotation rotation = BlockRotation.random(this.random);
//            ReficuleVillageGenerator.addPieces(structureManager, pos, rotation, this.children);
//            this.setBoundingBoxFromChildren();
//        }
//    }
//}