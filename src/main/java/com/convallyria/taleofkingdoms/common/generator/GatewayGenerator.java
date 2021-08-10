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
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GatewayGenerator {

    private static final ResourceLocation GATEWAY = new ResourceLocation(TaleOfKingdoms.MODID, "gateway/gateway");
    private static final ResourceLocation BARS = new ResourceLocation(TaleOfKingdoms.MODID, "gateway/bars");

    public static void addPieces(StructureManager manager, BlockPos pos, Rotation blockRotation, StructurePieceAccessor structurePiecesHolder, Random random) {
        final Direction direction = Direction.getRandom(ThreadLocalRandom.current());
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
        private final Rotation rotation;
        private final ResourceLocation template;

        public GatewayPiece(StructureManager structureManager, ResourceLocation identifier, BlockPos blockPos, Rotation blockRotation, int i) {
            super(TaleOfKingdoms.GATEWAY, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
            this.templatePosition = templatePosition;
            this.rotation = blockRotation;
            this.template = identifier;
            createPlacementData(blockRotation, identifier);
        }

        public GatewayPiece(ServerLevel serverWorld, CompoundTag nbtCompound) {
            super(TaleOfKingdoms.GATEWAY, nbtCompound, serverWorld, (identifier) -> {
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
        protected void handleDataMarker(String metadata, BlockPos blockPos, ServerLevelAccessor serverWorldAccess,
                                      Random random, BoundingBox blockBox) {
            switch (metadata) {
                case "ReficuleSoldier" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, pos);
                case "ReficuleArcher" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, pos);
                case "ReficuleMage" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, pos);
            }
        }
    }
}
