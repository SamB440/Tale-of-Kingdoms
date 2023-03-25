package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.BanditCampGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class BanditCampStructure extends Structure {

    public static final Codec<BanditCampStructure> CODEC = createCodec(BanditCampStructure::new);

    public BanditCampStructure(Config config) {
        super(config);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        double percent = Math.random() * 100;
        if (percent >= TaleOfKingdoms.CONFIG.mainConfig.banditCampSpawnRate) return Optional.empty();
        return getStructurePosition(context, Heightmap.Type.WORLD_SURFACE_WG, (collector) -> this.addPieces(collector, context));
    }

    @Override
    public StructureType<?> getType() {
        return TOKStructures.BANDIT_CAMP_TYPE;
    }

    private void addPieces(StructurePiecesCollector collector, Context context) {
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkRandom chunkRandom = context.random();
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();
        int y = context.chunkGenerator().getHeightInGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockRotation blockRotation = BlockRotation.random(chunkRandom);
        BanditCampGenerator.addPieces(context.structureTemplateManager(), blockPos, blockRotation, collector, chunkRandom);
    }
}