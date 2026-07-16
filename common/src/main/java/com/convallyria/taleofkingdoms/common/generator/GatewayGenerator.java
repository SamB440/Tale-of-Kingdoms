package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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

public class GatewayGenerator {

    private static final ResourceLocation GATEWAY = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "gateway/gateway");
    private static final ResourceLocation BARS = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "gateway/bars");

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation blockRotation, StructurePieceAccessor structurePiecesHolder, RandomSource random) {
        final Direction direction = Direction.getRandom(random);
        GatewayPiece gateway = new GatewayPiece(manager, GATEWAY, pos.subtract(new Vec3i(0, 1, 0)), Rotation.NONE, 0);
        gateway.setOrientation(direction);
        structurePiecesHolder.addPiece(gateway);
        BlockPos startPos = pos.offset(new Vec3i(7, 0, 5)).subtract(new Vec3i(0, 2, 0));
        int times = pos.getY() - 1;
        for (int i = 0; i < times; i++) {
            GatewayPiece bars = new GatewayPiece(manager, BARS, startPos, Rotation.NONE, 0);
            bars.setOrientation(direction);
            structurePiecesHolder.addPiece(bars);
            startPos = startPos.subtract(new Vec3i(0, 1, 0));
        }
    }

    public static class GatewayPiece extends TemplateStructurePiece {

        public GatewayPiece(StructureTemplateManager structureManager, ResourceLocation identifier, BlockPos blockPos, Rotation blockRotation, int i) {
            super(TOKStructures.GATEWAY.get(), 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation), blockPos);
        }

        public GatewayPiece(StructureTemplateManager structureManager, CompoundTag nbtCompound) {
            super(TOKStructures.GATEWAY.get(), nbtCompound, structureManager, (identifier) -> createPlacementData(null));
        }

        public GatewayPiece(StructurePieceSerializationContext structureContext, CompoundTag nbtCompound) {
            super(TOKStructures.GATEWAY.get(), nbtCompound, structureContext.structureTemplateManager(), (identifier) -> createPlacementData(null));
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
            switch (metadata) {
                case "ReficuleSoldier" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER.get(), serverWorldAccess, blockPos);
                case "ReficuleArcher" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN.get(), serverWorldAccess, blockPos);
                case "ReficuleMage" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE.get(), serverWorldAccess, blockPos);
            }
        }
    }
}
