package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntitySpawnCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class EntitySpawnEvent {

    @Inject(method = "spawnEntity", at = @At(value = "INVOKE"), cancellable = true)
    public void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!EntitySpawnCallback.EVENT.invoker().spawn(entity)) {
            cir.setReturnValue(false);
        }
    }
}
