package com.convallyria.taleofkingdoms.mixin.structure;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = StructureBlockBlockEntity.class, priority = 1001) // Carpet support - use ours
public class StructureBlockUnlimit {
    
    @ModifyConstant(method = "fromTag", constant = @Constant(intValue = 48))
    public int readNbtUpper(int value) {
        return 512;
    }
    
    @ModifyConstant(method = "fromTag", constant = @Constant(intValue = -48))
    public int readNbtLower(int value) {
        return -512;
    }
    
    /**
     * @reason Increase the distance that the bounding box can be seen up to 256 blocks
     * @author SamB440/Cotander
     */
    @ModifyConstant(method = "getRenderDistance", constant = @Constant(doubleValue = 96d))
    @Environment(EnvType.CLIENT)
    public double getRenderDistance(double value) {
        return 256d;
    }
}
