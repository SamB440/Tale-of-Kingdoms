package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
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
import net.minecraft.world.biome.GenerationSettings;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiningTownGenerator {

    //private static final Identifier MINING_TOWN = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town");
    private static final Identifier ONE = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_one");
    private static final Identifier TWO = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_two");
    private static final Identifier THREE = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_three");
    private static final Identifier PIT = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_pit");


    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        final Direction direction = Direction.random(ThreadLocalRandom.current());
        MiningTownPiece onePiece = new MiningTownPiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), BlockRotation.NONE, 0);
        onePiece.setOrientation(direction);
        pieces.add(onePiece);

        BlockPos middlePos = pos.add(new Vec3i(48, 0, 0));
        MiningTownPiece middlePiece = new MiningTownPiece(manager, PIT, middlePos, BlockRotation.NONE, 0);
        middlePiece.setOrientation(direction);
        pieces.add(middlePiece);

        BlockPos threePos = middlePos.add(new Vec3i(32, 0, 0));
        MiningTownPiece threePiece = new MiningTownPiece(manager, THREE, threePos, BlockRotation.NONE, 0);
        threePiece.setOrientation(direction);
        pieces.add(threePiece);

        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
        MiningTownPiece middleTwoPiece = new MiningTownPiece(manager, TWO, middleTwoPos, BlockRotation.NONE, 0);
        middleTwoPiece.setOrientation(direction);
        pieces.add(middleTwoPiece);

        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
        MiningTownPiece towerPiece = new MiningTownPiece(manager, ONE, towerPos, BlockRotation.NONE, 0);
        towerPiece.setOrientation(direction);
        pieces.add(towerPiece);
    }

    public static class MiningTownPiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public MiningTownPiece(StructureManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
            super(TaleOfKingdoms.MINING_TOWN, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
            this.pos = pos;
            this.rotation = blockRotation;
            this.template = identifier;
            createPlacementData(blockRotation, identifier);
        }

        public MiningTownPiece(ServerWorld serverWorld, NbtCompound nbtCompound) {
            super(TaleOfKingdoms.MINING_TOWN, nbtCompound, serverWorld, (identifier) -> {
                String rot = nbtCompound.getString("Rot");
                BlockRotation rotation = rot.isEmpty() ? BlockRotation.NONE : BlockRotation.valueOf(rot);
                return createPlacementData(rotation, identifier);
            });
            String rot = nbtCompound.getString("Rot");
            BlockRotation rotation = rot.isEmpty() ? BlockRotation.NONE : BlockRotation.valueOf(rot);
            this.template = new Identifier(nbtCompound.getString("Template"));
            this.rotation = rotation;
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

            if (percent > 60) {
                switch (metadata) {
                    case "Bandit" -> EntityUtils.spawnEntity(EntityTypes.BANDIT, serverWorldAccess, pos);
                    case "Foreman" -> EntityUtils.spawnEntity(EntityTypes.FOREMAN, serverWorldAccess, pos);
                }
            }
        }
    }
}
