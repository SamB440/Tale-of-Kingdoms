package com.convallyria.taleofkingdoms.mixin.packet;

import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = UpdateStructureBlockC2SPacket.class, priority = 999) // Carpet support - use ours
public class ClientUpdateStructureBlockUnlimit {
    
    @ModifyConstant(method = "read", constant = @Constant(intValue = 48), require = 0)
    public int reinitUpper(int value) {
        return 512;
    }
    
    @ModifyConstant(method = "read", constant = @Constant(intValue = -48), require = 0)
    public int reinitLower(int value) {
        return -512;
    }
}
