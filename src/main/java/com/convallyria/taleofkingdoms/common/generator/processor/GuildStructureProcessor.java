package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GuildStructureProcessor extends StructureProcessor {

    public static final GuildStructureProcessor INSTANCE = new GuildStructureProcessor();
    public static final Codec<GuildStructureProcessor> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    private final List<SchematicOptions> options;

    public GuildStructureProcessor(SchematicOptions... options) {
        this.options = Arrays.asList(options);
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlacementData) {
        ServerLevelAccessor serverWorldAccess = (ServerLevelAccessor) worldView;
        StructureTemplate.StructureBlockInfo air = new StructureTemplate.StructureBlockInfo(structureBlockInfo2.pos, Blocks.AIR.defaultBlockState(), new CompoundTag());
        if (structureBlockInfo2.state.getBlock() instanceof StructureBlock) {
            String metadata = structureBlockInfo2.nbt.getString("metadata");
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (api.isEmpty()) return structureBlockInfo2;
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            TaleOfKingdoms.LOGGER.debug(structureBlockInfo2.pos);
            if (instance.isEmpty()) return structureBlockInfo2;

            if (metadata.equalsIgnoreCase("Gateway")) {
                if (!instance.get().isLoaded()) instance.get().getReficuleAttackLocations().add(structureBlockInfo2.pos);
                return air;
            }

            if (options.contains(SchematicOptions.NO_ENTITIES)) return air;

            BlockPos spawnPos = structureBlockInfo2.pos.offset(0.5, 0, 0.5);
            try {
                EntityType type = (EntityType<?>) EntityTypes.class.getField(metadata.toUpperCase(TaleOfKingdoms.DEFAULT_LOCALE)).get(EntityTypes.class);
                if (options.contains(SchematicOptions.IGNORE_DEFENDERS)
                        && (type == EntityTypes.GUILDGUARD || type == EntityTypes.GUILDARCHER)) {
                    return air;
                }

                if (type != EntityTypes.GUILDGUARD && type != EntityTypes.GUILDARCHER) {
                    Optional<? extends Entity> guildEntity = instance.get().getGuildEntity(serverWorldAccess.getLevel(), type);
                    if (type == EntityTypes.GUILDMASTER) {
                        guildEntity = instance.get().getGuildMaster(serverWorldAccess.getLevel());
                    }

                    if (guildEntity.isEmpty()) {
                        EntityUtils.spawnEntity(type, serverWorldAccess, spawnPos);
                    }
                } else EntityUtils.spawnEntity(type, serverWorldAccess, spawnPos);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return air;
        }
        return structureBlockInfo2;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GUILD_PROCESSOR;
    }
}