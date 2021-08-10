package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.serialization.Codec;
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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GatewayStructureProcessor extends StructureProcessor {

    public static final GatewayStructureProcessor INSTANCE = new GatewayStructureProcessor();
    public static final Codec<GatewayStructureProcessor> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    public GatewayStructureProcessor() { }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlacementData) {
        ServerLevelAccessor serverWorldAccess = (ServerLevelAccessor) worldView;
        BlockPos newPos = blockPos.subtract(new Vec3i(6, 0, 6));
        if (structureBlockInfo2.state.getBlock() instanceof StructureBlock) {
            String metadata = structureBlockInfo2.nbt.getString("metadata");
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (api.isEmpty()) return structureBlockInfo2;
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) return structureBlockInfo2;

            switch (metadata) {
                case "ReficuleSoldier" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
                    return null;
                }
                case "ReficuleArcher" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
                    return null;
                }
                case "ReficuleMage" -> {
                    Mob entity = EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, newPos);
                    instance.get().getReficuleAttackers().add(entity.getUUID());
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