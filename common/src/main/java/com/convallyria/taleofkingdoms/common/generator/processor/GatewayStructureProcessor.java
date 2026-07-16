package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class GatewayStructureProcessor extends StructureProcessor {

    public static final GatewayStructureProcessor INSTANCE = new GatewayStructureProcessor();
    public static final MapCodec<GatewayStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    public GatewayStructureProcessor() { }

    // Used for the guild attack - not the general structure spawn. That's why we add to reficule attackers.
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlaceSettings data) {
        ServerLevelAccessor serverWorldAccess = (ServerLevelAccessor) world;
        BlockPos newPos = pos.subtract(new Vec3i(6, 0, 6));
        if (currentBlockInfo.state().getBlock() instanceof StructureBlock) {
            String metadata = currentBlockInfo.nbt().getString("metadata");
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return currentBlockInfo;
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) return currentBlockInfo;

            switch (metadata) {
                case "ReficuleSoldier" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER.get(), serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
                    return null;
                }
                case "ReficuleArcher" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN.get(), serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
                    return null;
                }
                case "ReficuleMage" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE.get(), serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
                    return null;
                }
            }
        }
        return currentBlockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.GATEWAY_PROCESSOR.get();
    }
}