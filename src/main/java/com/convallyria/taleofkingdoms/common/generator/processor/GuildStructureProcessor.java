package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.Codec;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GuildStructureProcessor extends StructureProcessor {

    public static final GuildStructureProcessor INSTANCE = new GuildStructureProcessor();
    public static final Codec<GuildStructureProcessor> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    public GuildStructureProcessor() { }

    @Nullable
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData) {
        ServerWorldAccess serverWorldAccess = (ServerWorldAccess) worldView;
        if (structureBlockInfo2.state.getBlock() instanceof StructureBlock) {
            String metadata = structureBlockInfo2.tag.getString("metadata");
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (!api.isPresent()) return structureBlockInfo2;
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            System.out.println("present? " + instance.isPresent());
            if (!instance.isPresent()) return structureBlockInfo2;
    
            switch (metadata) {
                case "Gateway": {
                    instance.get().getReficuleAttackLocations().add(structureBlockInfo2.pos);
                    return null;
                }
            }
    
            BlockPos spawnPos = structureBlockInfo2.pos.add(0.5, 0, 0.5);
            try {
                EntityType type = (EntityType<?>) EntityTypes.class.getField(metadata.toUpperCase()).get(EntityTypes.class);
                EntityUtils.spawnEntity(type, serverWorldAccess, spawnPos);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return null;
        }
        return structureBlockInfo2;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GUILD_PROCESSOR;
    }
}