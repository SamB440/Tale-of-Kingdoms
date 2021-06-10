package com.convallyria.taleofkingdoms.mixin.structure;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(StructureBlockBlockEntity.class)
public interface StructureBlockAccessor {

    @Invoker("streamCornerPos")
    Stream<BlockPos> streamCornerPos(BlockPos pos2, BlockPos pos3);

    @Invoker("updateBlockMode")
    void updateBlockMode();
}