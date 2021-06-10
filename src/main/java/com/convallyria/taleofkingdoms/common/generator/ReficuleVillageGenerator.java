package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
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
        ReficuleVillagePiece onePiece = new ReficuleVillagePiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), BlockRotation.NONE, 0);
        onePiece.setOrientation(direction);
        pieces.add(onePiece);

        BlockPos middlePos = pos.add(new Vec3i(48, 0, 0));
        ReficuleVillagePiece middlePiece = new ReficuleVillagePiece(manager, MIDDLE, middlePos, BlockRotation.NONE, 0);
        middlePiece.setOrientation(direction);
        pieces.add(middlePiece);

        BlockPos threePos = middlePos.add(new Vec3i(32, 0, 0));
        ReficuleVillagePiece threePiece = new ReficuleVillagePiece(manager, THREE, threePos, BlockRotation.NONE, 0);
        threePiece.setOrientation(direction);
        pieces.add(threePiece);

        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
        ReficuleVillagePiece middleTwoPiece = new ReficuleVillagePiece(manager, MIDDLE_TWO, middleTwoPos, BlockRotation.NONE, 0);
        middleTwoPiece.setOrientation(direction);
        pieces.add(middleTwoPiece);

        BlockPos fourPos = middleTwoPos.add(new Vec3i(0, 0, 13)).add(new Vec3i(32, 0, 0));
        ReficuleVillagePiece fourPiece = new ReficuleVillagePiece(manager, FOUR, fourPos, BlockRotation.NONE, 0);
        fourPiece.setOrientation(direction);
        pieces.add(fourPiece);

        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
        ReficuleVillagePiece towerPiece = new ReficuleVillagePiece(manager, TOWER, towerPos, BlockRotation.NONE, 0);
        towerPiece.setOrientation(direction);
        pieces.add(towerPiece);
    }

    public static class ReficuleVillagePiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public ReficuleVillagePiece(StructureManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
            this.pos = pos;
            this.rotation = blockRotation;
            this.template = identifier;
            createPlacementData(blockRotation, identifier);
        }

        public ReficuleVillagePiece(ServerWorld serverWorld, NbtCompound nbtCompound) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, nbtCompound, serverWorld, (identifier) -> {
                return createPlacementData(BlockRotation.valueOf(nbtCompound.getString("Rot")), identifier);
            });
            this.template = new Identifier(nbtCompound.getString("Template"));
            this.rotation = BlockRotation.valueOf(nbtCompound.getString("Rot"));
            createPlacementData(rotation, template);
        }

        private static StructurePlacementData createPlacementData(BlockRotation blockRotation, Identifier identifier) {
            return (new StructurePlacementData())
                    .setRotation(blockRotation)
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
                                      BlockBox boundingBox) {
            double percent = Math.random() * 100;
            if (metadata.equals("Survivor")) {
                if (percent > 20) {
                    EntityUtils.spawnEntity(EntityTypes.LONEVILLAGER, serverWorldAccess, pos);
                }
                return;
            }

            if (percent > 60) {
                switch (metadata) {
                    case "ReficuleSoldier":
                        EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, pos);
                        break;
                    case "ReficuleArcher":
                        EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, pos);
                        break;
                    case "ReficuleMage":
                        EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, pos);
                        break;
                }
            }
        }
    }
}
