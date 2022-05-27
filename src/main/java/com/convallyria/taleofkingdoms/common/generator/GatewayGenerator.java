package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TOKStructures;
import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GatewayGenerator {

    private static final Identifier GATEWAY = new Identifier(TaleOfKingdoms.MODID, "gateway/gateway");
    private static final Identifier BARS = new Identifier(TaleOfKingdoms.MODID, "gateway/bars");

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation blockRotation, StructurePiecesHolder structurePiecesHolder, Random random) {
        final Direction direction = Direction.random(ThreadLocalRandom.current());
        GatewayPiece gateway = new GatewayPiece(manager, GATEWAY, pos.subtract(new Vec3i(0, 1, 0)), BlockRotation.NONE, 0);
        gateway.setOrientation(direction);
        structurePiecesHolder.addPiece(gateway);
        BlockPos startPos = pos.add(new Vec3i(7, 0, 5)).subtract(new Vec3i(0, 2, 0));
        int times = pos.getY() - 1;
        for (int i = 0; i < times; i++) {
            GatewayPiece bars = new GatewayPiece(manager, BARS, startPos, BlockRotation.NONE, 0);
            bars.setOrientation(direction);
            structurePiecesHolder.addPiece(bars);
            startPos = startPos.subtract(new Vec3i(0, 1, 0));
        }
    }

    public static class GatewayPiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public GatewayPiece(StructureManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
            super(TOKStructures.GATEWAY, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
            this.pos = blockPos;
            this.rotation = blockRotation;
            this.template = identifier;
            createPlacementData(blockRotation, identifier);
        }

        public GatewayPiece(ServerWorld serverWorld, NbtCompound nbtCompound) {
            super(TOKStructures.GATEWAY, nbtCompound, serverWorld.getStructureManager(), (identifier) -> {
                return createPlacementData(BlockRotation.valueOf(nbtCompound.getString("Rot")), identifier);
            });
            this.template = new Identifier(nbtCompound.getString("Template"));
            this.rotation = BlockRotation.valueOf(nbtCompound.getString("Rot"));
            createPlacementData(rotation, template);
        }

        public GatewayPiece(StructureContext structureContext, NbtCompound nbtCompound) {
            super(TOKStructures.GATEWAY, nbtCompound, structureContext.structureManager(), (identifier) -> {
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
        protected void handleMetadata(String metadata, BlockPos blockPos, ServerWorldAccess serverWorldAccess,
                                      Random random, BlockBox blockBox) {
            switch (metadata) {
                case "ReficuleSoldier" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, pos);
                case "ReficuleArcher" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, pos);
                case "ReficuleMage" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, pos);
            }
        }
    }
}
