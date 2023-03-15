package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldView;
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
    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        ServerWorldAccess serverWorldAccess = (ServerWorldAccess) world;
        StructureTemplate.StructureBlockInfo air = new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos, Blocks.AIR.getDefaultState(), new NbtCompound());
        if (currentBlockInfo.state.getBlock() instanceof StructureBlock) {
            String metadata = currentBlockInfo.nbt.getString("metadata");
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return currentBlockInfo;
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            TaleOfKingdoms.LOGGER.debug(currentBlockInfo.pos);
            if (instance.isEmpty()) return currentBlockInfo;

            if (metadata.equalsIgnoreCase("Gateway")) {
                if (!instance.get().isLoaded()) instance.get().getReficuleAttackLocations().add(currentBlockInfo.pos);
                return air;
            }

            if (options.contains(SchematicOptions.NO_ENTITIES)) return air;

            Vec3d spawnPos = currentBlockInfo.pos.toCenterPos();
            try {
                EntityType type = (EntityType<?>) EntityTypes.class.getField(metadata.toUpperCase(TaleOfKingdoms.DEFAULT_LOCALE)).get(EntityTypes.class);
                if (type == null) return air;
                if (options.contains(SchematicOptions.IGNORE_DEFENDERS)
                        && (type == EntityTypes.GUILDGUARD || type == EntityTypes.GUILDARCHER)) {
                    return air;
                }

                if (type != EntityTypes.GUILDGUARD && type != EntityTypes.GUILDARCHER) {
                    Optional guildEntity = instance.get().getGuildEntity(serverWorldAccess.toServerWorld(), type);
                    if (type == EntityTypes.GUILDMASTER) {
                        guildEntity = instance.get().getGuildMaster(serverWorldAccess.toServerWorld());
                    }

                    if (guildEntity.isEmpty()) {
                        EntityUtils.spawnEntity(type, serverWorldAccess, BlockPos.ofFloored(spawnPos));
                    }
                } else EntityUtils.spawnEntity(type, serverWorldAccess, BlockPos.ofFloored(spawnPos));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return air;
        }
        return currentBlockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GUILD_PROCESSOR;
    }
}