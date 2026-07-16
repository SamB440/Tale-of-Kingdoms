package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.gamerule.TOKGameRules;
import com.convallyria.taleofkingdoms.common.generator.GuildGenerator;
import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.MapCodec;
import dev.architectury.utils.GameInstance;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

/**
 * Generates the Guild once per world, placed by a single concentric-rings structure set
 * within 2000 blocks of 0,0 (see {@code data/taleofkingdoms/worldgen/structure_set/guild.json}).
 */
public class GuildStructure extends Structure {

    public static final MapCodec<GuildStructure> CODEC = simpleCodec(GuildStructure::new);

    public GuildStructure(StructureSettings config) {
        super(config);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        // Gamerule off -> legacy behaviour: the player places the guild via the start screen.
        final MinecraftServer server = GameInstance.getServer();
        if (server != null && !server.getGameRules().getBoolean(TOKGameRules.AUTO_GENERATE_GUILD)) {
            return Optional.empty();
        }

        // Never generate a second guild: pre-gamerule worlds (or manual placements) already have one.
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            final Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent() && instance.get().getStart() != null) return Optional.empty();
        }

        // The structure's biome gate is all of is_overworld so that modded biomes (Biomes O'
        // Plenty etc.) qualify even when untagged; instead, explicitly exclude water here.
        if (isAvoidedBiome(context)) return Optional.empty();

        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (collector) -> this.addPieces(collector, context));
    }

    private static boolean isAvoidedBiome(GenerationContext context) {
        final ChunkPos chunkPos = context.chunkPos();
        final int x = chunkPos.getMiddleBlockX();
        final int z = chunkPos.getMiddleBlockZ();
        final int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        final Holder<Biome> biome = context.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), context.randomState().sampler());
        return biome.is(TOKBiomeTags.GUILD_AVOID);
    }

    @Override
    public StructureType<?> type() {
        return TOKStructures.GUILD_TYPE.get();
    }

    private void addPieces(StructurePiecesBuilder collector, GenerationContext context) {
        final ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos blockPos = new BlockPos(x, y - GuildGenerator.SURFACE_DEPTH, z);
        GuildGenerator.addPieces(context.structureTemplateManager(), blockPos, collector);
    }
}
