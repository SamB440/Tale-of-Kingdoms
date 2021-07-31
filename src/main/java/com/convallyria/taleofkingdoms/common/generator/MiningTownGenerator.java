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

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiningTownGenerator {

    //private static final Identifier MINING_TOWN = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town");
    private static final Identifier ONE = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_one");
    private static final Identifier TWO = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_two");
    private static final Identifier THREE = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_three");
    private static final Identifier PIT = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_pit");
    private static final Identifier TUNNEL = new Identifier(TaleOfKingdoms.MODID, "mining_town/mining_town_tunnel");


    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        final Direction direction = Direction.random(ThreadLocalRandom.current());
        //MiningTownPiece onePiece = new MiningTownPiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), BlockRotation.NONE, 0);
        //onePiece.setOrientation(direction);
        //pieces.add(onePiece);

        MiningTownPiece one = new MiningTownPiece(manager, ONE, pos.add(36, 0, -23), BlockRotation.NONE, 0);
        one.setOrientation(direction);
        pieces.add(one);

        MiningTownPiece middlePiece = new MiningTownPiece(manager, PIT, pos.subtract(new Vec3i(0, 23, 0)), BlockRotation.NONE, 0);
        middlePiece.setOrientation(direction);
        pieces.add(middlePiece);

        MiningTownPiece two = new MiningTownPiece(manager, TWO, pos.subtract(new Vec3i(0, 0, 41)), BlockRotation.NONE, 0);
        two.setOrientation(direction);
        pieces.add(two);

        MiningTownPiece three = new MiningTownPiece(manager, THREE, pos.subtract(new Vec3i(39, 0, 0)), BlockRotation.NONE, 0);
        three.setOrientation(direction);
        pieces.add(three);

        MiningTownPiece tunnel = new MiningTownPiece(manager, TUNNEL, pos.subtract(new Vec3i(58, 23, 7)), BlockRotation.NONE, 0);
        tunnel.setOrientation(direction);
        pieces.add(tunnel);
    }

    public static class MiningTownPiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public MiningTownPiece(StructureManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
            super(TaleOfKingdoms.MINING_TOWN, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
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
