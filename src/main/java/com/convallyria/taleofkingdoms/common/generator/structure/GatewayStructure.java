package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class GatewayStructure extends Structure {

    public static final MapCodec<GatewayStructure> CODEC = simpleCodec(GatewayStructure::new);

    public GatewayStructure(StructureSettings config) {
        super(config);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        double percent = Math.random() * 100;
        if (percent >= TaleOfKingdoms.CONFIG.mainConfig.gateWaySpawnRate) return Optional.empty();
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (collector) -> this.addPieces(collector, context));
    }

    @Override
    public StructureType<?> type() {
        return TOKStructures.GATEWAY_TYPE;
    }

    private void addPieces(StructurePiecesBuilder collector, GenerationContext context) {
        final ChunkPos chunkPos = context.chunkPos();
        final WorldgenRandom chunkRandom = context.random();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos blockPos = new BlockPos(x, y, z);
        Rotation blockRotation = Rotation.getRandom(chunkRandom);
        GatewayGenerator.addPieces(context.structureTemplateManager(), blockPos, blockRotation, collector, chunkRandom);
    }
}