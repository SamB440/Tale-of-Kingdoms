package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReficuleVillageGenerator {

    private static final ResourceLocation ONE = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_one");
    private static final ResourceLocation THREE = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_three");
    private static final ResourceLocation FOUR = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_four");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle");
    private static final ResourceLocation MIDDLE_TWO = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle_two");
    private static final ResourceLocation TOWER = new ResourceLocation(TaleOfKingdoms.MODID, "reficule_village/reficule_village_tower");

    public static void addPieces(StructureManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces) {
        final Direction direction = Direction.getRandom(ThreadLocalRandom.current());
        ReficuleVillagePiece onePiece = new ReficuleVillagePiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), Rotation.NONE, 0);
        onePiece.setOrientation(direction);
        pieces.add(onePiece);

        BlockPos middlePos = pos.offset(new Vec3i(48, 0, 0));
        ReficuleVillagePiece middlePiece = new ReficuleVillagePiece(manager, MIDDLE, middlePos, Rotation.NONE, 0);
        middlePiece.setOrientation(direction);
        pieces.add(middlePiece);

        BlockPos threePos = middlePos.offset(new Vec3i(32, 0, 0));
        ReficuleVillagePiece threePiece = new ReficuleVillagePiece(manager, THREE, threePos, Rotation.NONE, 0);
        threePiece.setOrientation(direction);
        pieces.add(threePiece);

        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
        ReficuleVillagePiece middleTwoPiece = new ReficuleVillagePiece(manager, MIDDLE_TWO, middleTwoPos, Rotation.NONE, 0);
        middleTwoPiece.setOrientation(direction);
        pieces.add(middleTwoPiece);

        BlockPos fourPos = middleTwoPos.offset(new Vec3i(0, 0, 13)).offset(new Vec3i(32, 0, 0));
        ReficuleVillagePiece fourPiece = new ReficuleVillagePiece(manager, FOUR, fourPos, Rotation.NONE, 0);
        fourPiece.setOrientation(direction);
        pieces.add(fourPiece);

        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
        ReficuleVillagePiece towerPiece = new ReficuleVillagePiece(manager, TOWER, towerPos, Rotation.NONE, 0);
        towerPiece.setOrientation(direction);
        pieces.add(towerPiece);
    }

    public static class ReficuleVillagePiece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation template;

        public ReficuleVillagePiece(StructureManager structureManager, ResourceLocation identifier, BlockPos blockPos, Rotation blockRotation, int i) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
            this.templatePosition = templatePosition;
            this.rotation = blockRotation;
            this.template = identifier;
            createPlacementData(blockRotation, identifier);
        }

        public ReficuleVillagePiece(ServerLevel serverWorld, CompoundTag nbtCompound) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, nbtCompound, serverWorld, (identifier) -> {
                return createPlacementData(Rotation.valueOf(nbtCompound.getString("Rot")), identifier);
            });
            this.template = new ResourceLocation(nbtCompound.getString("Template"));
            this.rotation = Rotation.valueOf(nbtCompound.getString("Rot"));
            createPlacementData(rotation, template);
        }

        private static StructurePlaceSettings createPlacementData(Rotation blockRotation, ResourceLocation identifier) {
            return (new StructurePlaceSettings())
                    .setRotation(blockRotation)
                    .setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor serverWorldAccess, Random random,
                                      BoundingBox boundingBox) {
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
