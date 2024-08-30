package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;

public class BanditCampGenerator {

    private static final Identifier SMALL_BANDIT_CAMP = Identifier.of(TaleOfKingdoms.MODID, "bandit_camp/small_bandit_camp");

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation blockRotation, StructurePiecesHolder structurePiecesHolder, Random random) {
        final Direction direction = Direction.random(random);
        BanditCampPiece campPiece = new BanditCampPiece(manager, SMALL_BANDIT_CAMP, pos, BlockRotation.NONE, 0);
        campPiece.setOrientation(direction);
        structurePiecesHolder.addPiece(campPiece);
    }

    public static class BanditCampPiece extends SimpleStructurePiece {

        public BanditCampPiece(StructureTemplateManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
            super(TOKStructures.BANDIT_CAMP, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation), blockPos);
        }

        public BanditCampPiece(StructureTemplateManager structureManager, NbtCompound nbtCompound) {
            super(TOKStructures.BANDIT_CAMP, nbtCompound, structureManager, (identifier) -> createPlacementData(null));
        }

        public BanditCampPiece(StructureContext structureContext, NbtCompound nbtCompound) {
            super(TOKStructures.BANDIT_CAMP, nbtCompound, structureContext.structureTemplateManager(), (identifier) -> createPlacementData(null));
        }

        private static StructurePlacementData createPlacementData(@Nullable BlockRotation rotation) {
            return (new StructurePlacementData())
                    .setRotation(rotation != null ? rotation : BlockRotation.random(Random.create()))
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos blockPos, ServerWorldAccess serverWorldAccess,
                                      Random random, BlockBox blockBox) {
            if (metadata.equals("Bandit")) {
                EntityUtils.spawnEntity(EntityTypes.BANDIT, serverWorldAccess, blockPos);
            }
        }
    }
}
