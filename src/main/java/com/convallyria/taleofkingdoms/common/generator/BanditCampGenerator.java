package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;

public class BanditCampGenerator {

    private static final ResourceLocation SMALL_BANDIT_CAMP = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "bandit_camp/small_bandit_camp");

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation blockRotation, StructurePieceAccessor structurePiecesHolder, RandomSource random) {
        final Direction direction = Direction.getRandom(random);
        BanditCampPiece campPiece = new BanditCampPiece(manager, SMALL_BANDIT_CAMP, pos, Rotation.NONE, 0);
        campPiece.setOrientation(direction);
        structurePiecesHolder.addPiece(campPiece);
    }

    public static class BanditCampPiece extends TemplateStructurePiece {

        public BanditCampPiece(StructureTemplateManager structureManager, ResourceLocation identifier, BlockPos blockPos, Rotation blockRotation, int i) {
            super(TOKStructures.BANDIT_CAMP, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation), blockPos);
        }

        public BanditCampPiece(StructureTemplateManager structureManager, CompoundTag nbtCompound) {
            super(TOKStructures.BANDIT_CAMP, nbtCompound, structureManager, (identifier) -> createPlacementData(null));
        }

        public BanditCampPiece(StructurePieceSerializationContext structureContext, CompoundTag nbtCompound) {
            super(TOKStructures.BANDIT_CAMP, nbtCompound, structureContext.structureTemplateManager(), (identifier) -> createPlacementData(null));
        }

        private static StructurePlaceSettings createPlacementData(@Nullable Rotation rotation) {
            return (new StructurePlaceSettings())
                    .setRotation(rotation != null ? rotation : Rotation.getRandom(RandomSource.create()))
                    .setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos blockPos, ServerLevelAccessor serverWorldAccess,
                                      RandomSource random, BoundingBox blockBox) {
            if (metadata.equals("Bandit")) {
                EntityUtils.spawnEntity(EntityTypes.BANDIT, serverWorldAccess, blockPos);
            }
        }
    }
}
