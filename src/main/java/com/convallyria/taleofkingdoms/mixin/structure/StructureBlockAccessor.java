package com.convallyria.taleofkingdoms.mixin.structure;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(StructureBlockBlockEntity.class)
public interface StructureBlockAccessor {

    @Invoker("findCorners")
    List<StructureBlockBlockEntity> findCorners(List<StructureBlockBlockEntity> structureBlockEntities);
    
    @Invoker("findStructureBlockEntities")
    List<StructureBlockBlockEntity> findStructureBlockEntities(BlockPos pos1, BlockPos pos2);
    
    @Invoker("makeBoundingBox")
    BlockBox makeBoundingBox(BlockPos center, List<StructureBlockBlockEntity> corners);

    @Invoker("updateBlockMode")
    void updateBlockMode();
}