package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReficuleVillageGenerator {

    private static final Identifier ONE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_one");
    private static final Identifier THREE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_three");
    private static final Identifier FOUR = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_four");
    private static final Identifier MIDDLE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle");
    private static final Identifier MIDDLE_TWO = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle_two");
    private static final Identifier TOWER = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_tower");

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        final Direction direction = Direction.random(ThreadLocalRandom.current());
        ReficuleVillagePiece onePiece = new ReficuleVillagePiece(manager, pos.subtract(new Vec3i(0, 6, 0)), ONE, BlockRotation.NONE);
        onePiece.setOrientation(direction);
        pieces.add(onePiece);

        BlockPos middlePos = pos.add(new Vec3i(48, 0, 0));
        ReficuleVillagePiece middlePiece = new ReficuleVillagePiece(manager, middlePos, MIDDLE, BlockRotation.NONE);
        middlePiece.setOrientation(direction);
        pieces.add(middlePiece);

        BlockPos threePos = middlePos.add(new Vec3i(32, 0, 0));
        ReficuleVillagePiece threePiece = new ReficuleVillagePiece(manager, threePos, THREE, BlockRotation.NONE);
        threePiece.setOrientation(direction);
        pieces.add(threePiece);

        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
        ReficuleVillagePiece middleTwoPiece = new ReficuleVillagePiece(manager, middleTwoPos, MIDDLE_TWO, BlockRotation.NONE);
        middleTwoPiece.setOrientation(direction);
        pieces.add(middleTwoPiece);

        BlockPos fourPos = middleTwoPos.add(new Vec3i(0, 0, 13)).add(new Vec3i(32, 0, 0));
        ReficuleVillagePiece fourPiece = new ReficuleVillagePiece(manager, fourPos, FOUR, BlockRotation.NONE);
        fourPiece.setOrientation(direction);
        pieces.add(fourPiece);

        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
        ReficuleVillagePiece towerPiece = new ReficuleVillagePiece(manager, towerPos, TOWER, BlockRotation.NONE);
        towerPiece.setOrientation(direction);
        pieces.add(towerPiece);
    }

    public static class ReficuleVillagePiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public ReficuleVillagePiece(StructureManager structureManager, CompoundTag compoundTag) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, compoundTag);
            this.template = new Identifier(compoundTag.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
            this.initializeStructureData(structureManager);
        }

        public ReficuleVillagePiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, 0);
            this.pos = pos;
            this.rotation = rotation;
            this.template = template;

            this.initializeStructureData(structureManager);
        }

        private void initializeStructureData(StructureManager structureManager) {
            Structure structure = structureManager.getStructureOrBlank(this.template);
            StructurePlacementData placementData = (new StructurePlacementData())
                    .setRotation(this.rotation)
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, placementData);
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
                                      BlockBox boundingBox) {
            if (metadata.equals("Survivor")) {
                double percent = Math.random() * 100;
                if (percent > 20) {
                    LoneVillagerEntity survivorEntity = EntityTypes.LONEVILLAGER.create(serverWorldAccess.toServerWorld());
                    survivorEntity.setPersistent();
                    survivorEntity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
                    survivorEntity.initialize(serverWorldAccess, serverWorldAccess.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
                    serverWorldAccess.spawnEntityAndPassengers(survivorEntity);
                }
            }
        }
    }
}
