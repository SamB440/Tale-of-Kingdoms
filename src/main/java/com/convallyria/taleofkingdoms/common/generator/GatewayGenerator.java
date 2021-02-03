package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
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

public class GatewayGenerator {

    private static final Identifier GATEWAY = new Identifier(TaleOfKingdoms.MODID, "gateway/gateway");
    private static final Identifier BARS = new Identifier(TaleOfKingdoms.MODID, "gateway/bars");

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        final Direction direction = Direction.random(ThreadLocalRandom.current());
        GatewayPiece gateway = new GatewayPiece(manager, pos, GATEWAY, BlockRotation.NONE);
        gateway.setOrientation(direction);
        pieces.add(gateway);

        BlockPos startPos = pos.add(new Vec3i(7, 0, 5)).subtract(new Vec3i(0, 1, 0));
        int times = pos.getY() - 1;
        for (int i = 0; i < times; i++) {
            GatewayPiece bars = new GatewayPiece(manager, startPos, BARS, BlockRotation.NONE);
            bars.setOrientation(direction);
            pieces.add(bars);
            startPos = startPos.subtract(new Vec3i(0, 1, 0));
        }
    }

    public static class GatewayPiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public GatewayPiece(StructureManager structureManager, CompoundTag compoundTag) {
            super(TaleOfKingdoms.REFICULE_VILLAGE, compoundTag);
            this.template = new Identifier(compoundTag.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
            this.initializeStructureData(structureManager);
        }

        public GatewayPiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
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
            // We don't have any metadata to handle.
        }
    }
}
