package com.convallyria.taleofkingdoms.common.generator;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class GuildGenerator {

    /** Same template that {@code Schematic.GUILD_CASTLE} pastes in the legacy flow. */
    private static final ResourceLocation GUILD = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild/guildnew");

    /**
     * How far below the surface the template origin sits. Matches the legacy paste flow, which
     * pastes at the player's position minus 20 (player feet = surface + 1).
     */
    public static final int SURFACE_DEPTH = 19;
    /** Origin offset above the template position; {@link ConquestInstance#rebuild} pastes at origin - 21. */
    public static final int ORIGIN_OFFSET = 21;

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, StructurePieceAccessor structurePiecesHolder) {
        // Rotation is fixed to NONE: ConquestInstance#rebuild re-pastes the template unrotated
        // at the recorded origin, so a rotated first generation would not line up.
        structurePiecesHolder.addPiece(new GuildPiece(manager, GUILD, pos));
    }

    public static class GuildPiece extends TemplateStructurePiece {

        public GuildPiece(StructureTemplateManager structureManager, ResourceLocation identifier, BlockPos blockPos) {
            super(TOKStructures.GUILD.get(), 0, structureManager, identifier, identifier.toString(), createPlacementData(), blockPos);
        }

        public GuildPiece(StructureTemplateManager structureManager, CompoundTag nbtCompound) {
            super(TOKStructures.GUILD.get(), nbtCompound, structureManager, (identifier) -> createPlacementData());
        }

        public GuildPiece(StructurePieceSerializationContext structureContext, CompoundTag nbtCompound) {
            super(TOKStructures.GUILD.get(), nbtCompound, structureContext.structureTemplateManager(), (identifier) -> createPlacementData());
        }

        private static StructurePlaceSettings createPlacementData() {
            return (new StructurePlaceSettings())
                    .setRotation(Rotation.NONE)
                    .setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
                    .addProcessor(JigsawReplacementProcessor.INSTANCE);
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator,
                                RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            super.postProcess(world, structureManager, chunkGenerator, random, chunkBox, chunkPos, pivot);
            // Worldgen runs off-thread and this fires once per intersecting chunk;
            // defer to the server thread and let registerGuild no-op after the first call.
            final BoundingBox box = this.getBoundingBox();
            final BlockPos templatePosition = this.templatePosition();
            final ServerLevel level = world.getLevel();
            level.getServer().execute(() -> registerGuild(box, templatePosition));
        }

        private static void registerGuild(BoundingBox box, BlockPos templatePosition) {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return;
            final Optional<ConquestInstance> optionalInstance = api.getConquestInstanceStorage().mostRecentInstance();
            if (optionalInstance.isEmpty()) {
                TaleOfKingdoms.LOGGER.warn("The Guild generated at {} but no conquest instance exists to record it.", templatePosition);
                return;
            }

            final ConquestInstance instance = optionalInstance.get();
            if (instance.getStart() != null) return; // Already recorded (or a guild was built manually).
            instance.setStart(new BlockPos(box.maxX(), box.maxY(), box.maxZ()));
            instance.setEnd(new BlockPos(box.minX(), box.minY(), box.minZ()));
            instance.setOrigin(templatePosition.offset(0, ORIGIN_OFFSET, 0));
            TaleOfKingdoms.LOGGER.info("The Guild has generated at {}.", templatePosition);
            final String worldName = api.getConquestInstanceStorage().getCurrentWorldName();
            if (worldName != null) instance.save(worldName);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos blockPos, ServerLevelAccessor serverWorldAccess,
                                        RandomSource random, BoundingBox blockBox) {
            // Mirrors GuildStructureProcessor, which handles these markers in the legacy paste flow.
            if (metadata.equalsIgnoreCase("Gateway")) {
                serverWorldAccess.getLevel().getServer().execute(() -> {
                    final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
                    if (api == null) return;
                    api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                        if (!instance.getReficuleAttackLocations().contains(blockPos)) {
                            instance.getReficuleAttackLocations().add(blockPos);
                        }
                    });
                });
                return;
            }

            final EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, metadata)).orElse(null);
            if (type == null) {
                TaleOfKingdoms.LOGGER.error("Unable to find entity {}", metadata);
                return;
            }
            EntityUtils.spawnEntity((EntityType<? extends Mob>) type, serverWorldAccess, blockPos);
        }
    }
}
