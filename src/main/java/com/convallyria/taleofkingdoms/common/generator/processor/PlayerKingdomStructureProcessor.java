package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PlayerKingdomStructureProcessor extends StructureProcessor {

    public static final PlayerKingdomStructureProcessor INSTANCE = new PlayerKingdomStructureProcessor();
    public static final MapCodec<PlayerKingdomStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final PlayerKingdom kingdom;
    private final ServerPlayerEntity player;

    public PlayerKingdomStructureProcessor() {
        this.kingdom = null;
        this.player = null;
    }

    public PlayerKingdomStructureProcessor(PlayerKingdom kingdom, ServerPlayerEntity player) {
        this.kingdom = kingdom;
        this.player = player;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        StructureTemplate.StructureBlockInfo air = new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), Blocks.AIR.getDefaultState(), new NbtCompound());
        if (currentBlockInfo.state().getBlock() instanceof StructureBlock) {
            String metadata = currentBlockInfo.nbt().getString("metadata");
            KingdomPOI.getFrom(metadata).ifPresent(poi -> poi.compute(kingdom, player, currentBlockInfo));
            return air;
        }
        return currentBlockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return TaleOfKingdoms.KINGDOM_PROCESSOR;
    }
}