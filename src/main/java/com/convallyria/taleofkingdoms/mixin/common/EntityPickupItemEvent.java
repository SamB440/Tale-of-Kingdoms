package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityPickupItemEvent {

    @Inject(method = "method_29499", at = @At(value = "INVOKE"))
    public void onPickup(ItemEntity itemEntity, CallbackInfo ci) {
        EntityPickupItemCallback.EVENT.invoker().pickup(((PlayerEntity) (Object) this), itemEntity.getStack().copy());
    }
}
