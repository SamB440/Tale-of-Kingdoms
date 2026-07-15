package com.convallyria.taleofkingdoms.common.generator.processor;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class PlayerKingdomStructureProcessor extends StructureProcessor {

    public static final PlayerKingdomStructureProcessor INSTANCE = new PlayerKingdomStructureProcessor();
    public static final MapCodec<PlayerKingdomStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final PlayerKingdom kingdom;
    private final ServerPlayer player;

    public PlayerKingdomStructureProcessor() {
        this.kingdom = null;
        this.player = null;
    }

    public PlayerKingdomStructureProcessor(PlayerKingdom kingdom, ServerPlayer player) {
        this.kingdom = kingdom;
        this.player = player;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlaceSettings data) {
        StructureTemplate.StructureBlockInfo air = new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), Blocks.AIR.defaultBlockState(), new CompoundTag());
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