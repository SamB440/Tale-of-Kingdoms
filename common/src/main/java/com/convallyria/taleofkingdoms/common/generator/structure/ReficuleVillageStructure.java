package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class ReficuleVillageStructure extends Structure {

    public static final MapCodec<ReficuleVillageStructure> CODEC = simpleCodec(ReficuleVillageStructure::new);

    public ReficuleVillageStructure(StructureSettings config) {
        super(config);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        double percent = Math.random() * 100;
        if (percent >= TaleOfKingdoms.CONFIG.mainConfig.reficuleVillageSpawnRate) return Optional.empty();
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (collector) -> this.addPieces(collector, context));
    }

    @Override
    public StructureType<?> type() {
        return TOKStructures.REFICULE_VILLAGE_TYPE.get();
    }

    public void addPieces(StructurePiecesBuilder collector, GenerationContext context) {
        final ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos pos = new BlockPos(x, y, z);
        Rotation rotation = Rotation.getRandom(context.random());
        ReficuleVillageGenerator.addPieces(context.structureTemplateManager(), pos, rotation, collector, context.random());
    }
}