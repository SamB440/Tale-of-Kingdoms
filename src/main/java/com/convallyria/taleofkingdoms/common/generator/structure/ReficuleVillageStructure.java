package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class ReficuleVillageStructure extends Structure {

    public static final Codec<ReficuleVillageStructure> CODEC = createCodec(ReficuleVillageStructure::new);

    public ReficuleVillageStructure(Config config) {
        super(config);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        double percent = Math.random() * 100;
        if (percent >= TaleOfKingdoms.CONFIG.mainConfig.reficuleVillageSpawnRate) return Optional.empty();
        return getStructurePosition(context, Heightmap.Type.WORLD_SURFACE_WG, (collector) -> this.addPieces(collector, context));
    }

    @Override
    public StructureType<?> getType() {
        return TOKStructures.REFICULE_VILLAGE_TYPE;
    }

    public void addPieces(StructurePiecesCollector collector, Context context) {
        final ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        int y = context.chunkGenerator().getHeightInGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation rotation = BlockRotation.random(context.random());
        ReficuleVillageGenerator.addPieces(context.structureTemplateManager(), pos, rotation, collector, context.random());
    }
}