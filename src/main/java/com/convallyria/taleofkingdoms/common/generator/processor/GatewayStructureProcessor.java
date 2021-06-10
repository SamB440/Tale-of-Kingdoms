package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.Codec;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GatewayStructureProcessor extends StructureProcessor {

    public static final GatewayStructureProcessor INSTANCE = new GatewayStructureProcessor();
    public static final Codec<GatewayStructureProcessor> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    public GatewayStructureProcessor() { }

    @Nullable
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData) {
        ServerWorldAccess serverWorldAccess = (ServerWorldAccess) worldView;
        BlockPos newPos = blockPos.subtract(new Vec3i(6, 0, 6));
        if (structureBlockInfo2.state.getBlock() instanceof StructureBlock) {
            String metadata = structureBlockInfo2.nbt.getString("metadata");
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (api.isEmpty()) return structureBlockInfo2;
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) return structureBlockInfo2;

            switch (metadata) {
                case "ReficuleSoldier" -> {
                    MobEntity entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUuid());
                    return null;
                }
                case "ReficuleArcher" -> {
                    MobEntity entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUuid());
                    return null;
                }
                case "ReficuleMage" -> {
                    MobEntity entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUuid());
                    return null;
                }
            }
        }
        return structureBlockInfo2;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GATEWAY_PROCESSOR;
    }
}