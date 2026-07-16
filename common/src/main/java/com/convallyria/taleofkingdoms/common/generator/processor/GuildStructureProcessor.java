package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

public class GuildStructureProcessor extends StructureProcessor {

    public static final GuildStructureProcessor INSTANCE = new GuildStructureProcessor();
    public static final MapCodec<GuildStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final List<SchematicOptions> options;

    public GuildStructureProcessor(SchematicOptions... options) {
        this.options = Arrays.asList(options);
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlaceSettings data) {
        ServerLevelAccessor serverWorldAccess = (ServerLevelAccessor) world;
        StructureTemplate.StructureBlockInfo air = new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), Blocks.AIR.defaultBlockState(), new CompoundTag());
        if (currentBlockInfo.state().getBlock() instanceof StructureBlock) {
            String metadata = currentBlockInfo.nbt().getString("metadata");
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return currentBlockInfo;
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            TaleOfKingdoms.LOGGER.debug(currentBlockInfo.pos());
            if (instance.isEmpty()) return currentBlockInfo;

            if (metadata.equalsIgnoreCase("Gateway")) {
                if (!instance.get().isLoaded()) instance.get().getReficuleAttackLocations().add(currentBlockInfo.pos());
                return air;
            }

            if (options.contains(SchematicOptions.NO_ENTITIES)) return air;

            Vec3 spawnPos = currentBlockInfo.pos().getCenter();
            final EntityType type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, metadata)).orElse(null);
            if (type == null) {
                TaleOfKingdoms.LOGGER.error("Unable to find entity {}", metadata);
                return air;
            }

            if (options.contains(SchematicOptions.IGNORE_DEFENDERS)
                    && (type == EntityTypes.GUILDGUARD.get() || type == EntityTypes.GUILDARCHER.get() || type == EntityTypes.GUILDVILLAGER.get())) {
                return air;
            }

            if (type != EntityTypes.GUILDGUARD.get() && type != EntityTypes.GUILDARCHER.get() && type != EntityTypes.GUILDVILLAGER.get()) {
                Optional guildEntity = instance.get().getGuildEntity(serverWorldAccess.getLevel(), type);
                if (type == EntityTypes.GUILDMASTER.get()) {
                    guildEntity = instance.get().getGuildMaster(serverWorldAccess.getLevel());
                }

                if (guildEntity.isEmpty()) {
                    EntityUtils.spawnEntity(type, serverWorldAccess, BlockPos.containing(spawnPos));
                }
            } else EntityUtils.spawnEntity(type, serverWorldAccess, BlockPos.containing(spawnPos));
            return air;
        }
        return currentBlockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GUILD_PROCESSOR.get();
    }
}