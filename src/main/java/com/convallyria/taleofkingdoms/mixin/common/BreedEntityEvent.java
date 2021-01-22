package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityBreedCallback;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public class BreedEntityEvent {
    
    @Inject(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;resetLoveTicks()V"))
    private void breed(ServerWorld serverWorld, AnimalEntity other, CallbackInfo ci) {
        AnimalEntity entity = (AnimalEntity) (Object) this;
        EntityBreedCallback.EVENT.invoker().breed(entity, other, entity.getLovingPlayer());
    }
}
