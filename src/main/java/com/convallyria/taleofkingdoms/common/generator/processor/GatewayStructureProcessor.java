package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
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
    public static final MapCodec<GatewayStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    public GatewayStructureProcessor() { }

    // Used for the guild attack - not the general structure spawn. That's why we add to reficule attackers.
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        ServerWorldAccess serverWorldAccess = (ServerWorldAccess) world;
        BlockPos newPos = pos.subtract(new Vec3i(6, 0, 6));
        if (currentBlockInfo.state().getBlock() instanceof StructureBlock) {
            String metadata = currentBlockInfo.nbt().getString("metadata");
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return currentBlockInfo;
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) return currentBlockInfo;

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
        return currentBlockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GATEWAY_PROCESSOR;
    }
}