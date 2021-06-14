package com.convallyria.taleofkingdoms.mixin.structure;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = StructureBlockBlockEntity.class, priority = 1001) // Carpet support - use ours
public class StructureBlockUnlimit {
    
    @ModifyConstant(method = "readNbt", constant = @Constant(intValue = 48))
    public int readNbtUpper(int value) {
        return 512;
    }
    
    @ModifyConstant(method = "readNbt", constant = @Constant(intValue = -48))
    public int readNbtLower(int value) {
        return -512;
    }
}
