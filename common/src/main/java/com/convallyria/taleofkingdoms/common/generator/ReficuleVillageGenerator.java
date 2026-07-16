package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.core.BlockPos;
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

public class ReficuleVillageGenerator {

    private static final ResourceLocation ONE = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_one");
    private static final ResourceLocation THREE = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_three");
    private static final ResourceLocation FOUR = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_four");
    private static final ResourceLocation MIDDLE = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle");
    private static final ResourceLocation MIDDLE_TWO = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle_two");
    private static final ResourceLocation TOWER = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_village/reficule_village_tower");

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, Rotation blockRotation, StructurePieceAccessor structurePiecesHolder, RandomSource random) {
        ReficuleVillagePiece onePiece = new ReficuleVillagePiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), Rotation.NONE, 0);
        structurePiecesHolder.addPiece(onePiece);

        BlockPos middlePos = pos.offset(new Vec3i(48, 0, 0));
        ReficuleVillagePiece middlePiece = new ReficuleVillagePiece(manager, MIDDLE, middlePos, Rotation.NONE, 0);
        structurePiecesHolder.addPiece(middlePiece);

        BlockPos threePos = middlePos.offset(new Vec3i(32, 0, 0));
        ReficuleVillagePiece threePiece = new ReficuleVillagePiece(manager, THREE, threePos, Rotation.NONE, 0);
        structurePiecesHolder.addPiece(threePiece);

        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
        ReficuleVillagePiece middleTwoPiece = new ReficuleVillagePiece(manager, MIDDLE_TWO, middleTwoPos, Rotation.NONE, 0);
        structurePiecesHolder.addPiece(middleTwoPiece);

        BlockPos fourPos = middleTwoPos.offset(new Vec3i(0, 0, 13)).offset(new Vec3i(32, 0, 0));
        ReficuleVillagePiece fourPiece = new ReficuleVillagePiece(manager, FOUR, fourPos, Rotation.NONE, 0);
        structurePiecesHolder.addPiece(fourPiece);

        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
        ReficuleVillagePiece towerPiece = new ReficuleVillagePiece(manager, TOWER, towerPos, Rotation.NONE, 0);
        structurePiecesHolder.addPiece(towerPiece);
    }

    public static class ReficuleVillagePiece extends TemplateStructurePiece {

        public ReficuleVillagePiece(StructureTemplateManager structureManager, ResourceLocation identifier, BlockPos blockPos, Rotation blockRotation, int i) {
            super(TOKStructures.REFICULE_VILLAGE.get(), 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation), blockPos);
        }

        public ReficuleVillagePiece(StructureTemplateManager structureManager, CompoundTag nbtCompound) {
            super(TOKStructures.REFICULE_VILLAGE.get(), nbtCompound, structureManager, (identifier) -> createPlacementData(null));
        }

        public ReficuleVillagePiece(StructurePieceSerializationContext structureContext, CompoundTag nbtCompound) {
            super(TOKStructures.REFICULE_VILLAGE.get(), nbtCompound, structureContext.structureTemplateManager(), (identifier) -> createPlacementData(null));
        }

        private static StructurePlaceSettings createPlacementData(@Nullable Rotation rotation) {
            return (new StructurePlaceSettings())
                    .setRotation(rotation != null ? rotation : Rotation.getRandom(RandomSource.create()))
                    .setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor world, net.minecraft.util.RandomSource random, BoundingBox boundingBox) {
            double percent = Math.random() * 100;
            if (metadata.equals("Survivor")) {
                if (percent > 20) {
                    EntityUtils.spawnEntity(EntityTypes.LONEVILLAGER.get(), world, pos);
                }
                return;
            }

            if (percent > 60) {
                switch (metadata) {
                    case "ReficuleSoldier" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER.get(), world, pos);
                    case "ReficuleArcher" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN.get(), world, pos);
                    case "ReficuleMage" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE.get(), world, pos);
                }
            }
        }
    }
}
