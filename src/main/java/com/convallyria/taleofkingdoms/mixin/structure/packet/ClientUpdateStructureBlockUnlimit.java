package com.convallyria.taleofkingdoms.mixin.structure.packet;

import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = UpdateStructureBlockC2SPacket.class, priority = 999) // Carpet support
public class ClientUpdateStructureBlockUnlimit {
    
    @ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 48), require = 0)
    public int reinitUpper(int value) {
        return 512;
    }
    
    @ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = -48), require = 0)
    public int reinitLower(int value) {
        return -512;
    }
}
